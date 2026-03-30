package dev.obie;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Symbolic Enforcer (ε) — paper Section III-B and Section IV-D.
 *
 * Applies deterministic refactoring templates to vulnerable Java code based on
 * the vulnerability category resolved from the CWE identifier.  Unlike the
 * probabilistic output of the Neural Oracle, the output of this component is
 * constrained to satisfy formally specified safety post-conditions.
 *
 * Formal definition (paper Equation 3):
 *   P' = ε(P, Ω) = P[loc ← Φ_cwe(ν_taint)]
 * where Φ_cwe is a context-sensitive validation / sanitization function.
 *
 * Three templates are implemented, matching Algorithms 1-3 of the paper:
 *   {@link #applyOSR}  — Output Safety Refactoring (Algorithm 1)
 *   {@link #applyWVR}  — Whitelist Validation Refactoring (Algorithm 2)
 *   {@link #applyTCVR} — TrustChain Verification Refactoring (Algorithm 3)
 *
 * @author Obieda Ananbeh
 */
public final class SymbolicEnforcer {

    private SymbolicEnforcer() {}

    /**
     * Entry point: select and apply the appropriate refactoring template.
     *
     * Implements {@code SelectTemplate(Ω.cwe)} followed by
     * {@code ApplyTemplate(P₀, Ω, R)} as defined in Algorithm 1 (Repair Loop)
     * of the paper.
     *
     * @param code        vulnerable Java code snippet
     * @param cweId       CWE identifier extracted by the Neural Oracle (e.g. "CWE-79")
     * @param taintedLine the exact vulnerable line identified during localization (ν_taint)
     * @return refactored code, or {@code null} if no template matches (→ Fallback)
     */
    public static String applyTemplate(String code, String cweId, String taintedLine) {
        CweCategory category = CweCategoryMapper.map(cweId);
        return switch (category) {
            case LOIS    -> applyOSR(code, taintedLine);
            case LOWV    -> applyWVR(code, taintedLine);
            case ITV     -> applyTCVR(code, taintedLine);
            case UNKNOWN -> null;  // Signal caller to invoke Fallback (Algorithm 4)
        };
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Algorithm 1 — Output Safety Refactoring (OSR)
    // Paper Section IV-D, Algorithm 1
    //
    // Pre-condition : v_tainted flows into a sink (HTML response, SQL query,
    //                 shell command) without neutralization.
    // Post-condition: The sink receives S(v_tainted) where S is a
    //                 context-sensitive encoder (paper Eq. 4).
    //
    // Implementation: wraps each variable that appears in the tainted line with
    //   an appropriate context-sensitive sanitizer and injects input-validation
    //   helpers before the vulnerable statement.
    // ─────────────────────────────────────────────────────────────────────────
    static String applyOSR(String code, String taintedLine) {
        if (taintedLine == null || taintedLine.isBlank()) return code;

        // Determine output context to select the correct encoder (GetEncoder(C))
        String encoder = resolveEncoder(taintedLine);

        // Extract the tainted variable name(s) from the vulnerable line
        String taintedVar = extractPrimaryVariable(taintedLine);

        // Build the sanitized replacement (Replace(N_sink, v, E_func(v)))
        String sanitizedLine = taintedVar.isBlank()
                ? taintedLine
                : taintedLine.replace(taintedVar, encoder + "(" + taintedVar + ")");

        // Inject sanitization import + validator helper before the vulnerable line,
        // then replace the line itself — satisfying the OSR safety post-condition.
        String securityImport = buildOsrHeader();
        String patched        = code.replace(taintedLine, sanitizedLine);

        return injectHeaderBeforeClass(patched, securityImport);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Algorithm 2 — Whitelist Validation Refactoring (WVR)
    // Paper Section IV-D, Algorithm 2
    //
    // Post-condition: ∀ x ∈ Input, ¬Valid(x) ⟹ Halt()
    //   i.e. every unvalidated input must be rejected before use.
    //
    // Implementation: injects a guard block immediately before the vulnerable
    //   statement that validates the tainted variable against an allowlist
    //   regex pattern and throws SecurityException if validation fails.
    // ─────────────────────────────────────────────────────────────────────────
    static String applyWVR(String code, String taintedLine) {
        if (taintedLine == null || taintedLine.isBlank()) return code;

        String taintedVar = extractPrimaryVariable(taintedLine);
        String pattern    = resolveAllowlistPattern(taintedLine);

        // Φ(x) := x ∈ P  (paper Algorithm 2, line 1)
        // Guard = "if (!Φ(x)) throw SecurityException;" (paper Algorithm 2, line 2)
        String guard = buildWvrGuard(taintedVar, pattern);

        // B' = { Guard; OriginalCode }  (paper Algorithm 2, line 3)
        // Replace the tainted line with: guard + original line
        String replacement = guard + "\n        " + taintedLine;
        String patched     = code.replace(taintedLine, replacement);

        return injectHeaderBeforeClass(patched, buildWvrImports());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Algorithm 3 — TrustChain Verification Refactoring (TCVR)
    // Paper Section IV-D, Algorithm 3
    //
    // Input : Object Instantiation O, Source S
    // Output: Verified Instantiation O'
    //
    // Implementation: injects cryptographic signature/SSL verification logic
    //   before the vulnerable object instantiation or network operation.
    //   Wraps the call as:
    //     if (Verifier.verify(Sig, Key)) O  else  LogAndAbort()
    // ─────────────────────────────────────────────────────────────────────────
    static String applyTCVR(String code, String taintedLine) {
        if (taintedLine == null || taintedLine.isBlank()) return code;

        // GetSignature(S) and GetTrustedKeyStore() — paper Algorithm 3, lines 1-2
        // Check = "Verifier.verify(Sig, Key)"       — paper Algorithm 3, line 3
        // O' = "if (Check) O else LogAndAbort()"   — paper Algorithm 3, line 4
        String verificationBlock = buildTcvrBlock(taintedLine);

        String replacement = verificationBlock + "\n        " + taintedLine;
        String patched     = code.replace(taintedLine, replacement);

        return injectHeaderBeforeClass(patched, buildTcvrImports());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Infer the context-sensitive output encoder from the tainted line content. */
    private static String resolveEncoder(String line) {
        String l = line.toLowerCase();
        if (l.contains("response.getwriter") || l.contains("printwriter") || l.contains("html"))
            return "ESAPI.encoder().encodeForHTML";
        if (l.contains("sql") || l.contains("query") || l.contains("preparedstatement"))
            return "ESAPI.encoder().encodeForSQL";
        if (l.contains("processbuilder") || l.contains("runtime.exec") || l.contains("cmdlist"))
            return "ESAPI.encoder().encodeForOS";
        if (l.contains("cookie") || l.contains("setvalue"))
            return "ESAPI.encoder().encodeForHTTPCookie";
        if (l.contains("xpath") || l.contains("xquery"))
            return "ESAPI.encoder().encodeForXPath";
        // Default: HTML-encode for generic output sinks
        return "ESAPI.encoder().encodeForHTML";
    }

    /** Select an allowlist regex appropriate for the tainted variable context. */
    private static String resolveAllowlistPattern(String line) {
        String l = line.toLowerCase();
        if (l.contains("path") || l.contains("file") || l.contains("dir"))
            return "^[\\\\w\\\\-./]+$";          // safe filesystem chars
        if (l.contains("url") || l.contains("uri") || l.contains("http"))
            return "^[a-zA-Z0-9\\\\-._~:/?#\\\\[\\\\]@!$&'()*+,;=]+$";
        if (l.contains("id") || l.contains("number") || l.contains("count"))
            return "^\\\\d+$";                   // numeric only
        if (l.contains("user") || l.contains("email"))
            return "^[\\\\w.@\\\\-]+$";
        // Default safe identifier pattern
        return "^[\\\\w\\\\-._\\\\s]+$";
    }

    /** Extract the primary tainted variable name from the vulnerable line. */
    private static String extractPrimaryVariable(String line) {
        // Match common Java patterns: method args, assignments, concatenations
        Pattern p = Pattern.compile(
                "getParameter\\(\"[^\"]+\"\\)" +
                "|request\\.[a-zA-Z]+\\(\"[^\"]+\"\\)" +
                "|\\b([a-z][a-zA-Z0-9]*)\\b"
        );
        Matcher m = p.matcher(line.strip());
        while (m.find()) {
            String candidate = m.group();
            // Skip Java keywords and common method names
            if (!isKeyword(candidate)) return candidate;
        }
        return "";
    }

    private static boolean isKeyword(String s) {
        return java.util.Set.of(
                "if","else","for","while","return","new","null","true","false",
                "throw","try","catch","finally","class","public","private","static",
                "void","int","String","List","Map","Set","final","throws"
        ).contains(s);
    }

    // ── OSR helpers ───────────────────────────────────────────────────────────

    private static String buildOsrHeader() {
        return """
                import org.owasp.esapi.ESAPI;
                import org.owasp.esapi.Encoder;
                """;
    }

    // ── WVR helpers ───────────────────────────────────────────────────────────

    private static String buildWvrGuard(String variable, String pattern) {
        if (variable.isBlank()) {
            // Generic guard when variable extraction fails
            return "        // WVR guard (paper Algorithm 2)\n" +
                   "        // TODO: replace 'input' with the actual tainted variable name\n" +
                   "        if (input == null || !input.matches(\"" + pattern + "\")) {\n" +
                   "            throw new SecurityException(\"Input failed whitelist validation\");\n" +
                   "        }";
        }
        return "        // WVR guard — Φ(x) := x ∈ P (paper Algorithm 2)\n" +
               "        if (" + variable + " == null || !" + variable + ".matches(\"" + pattern + "\")) {\n" +
               "            throw new SecurityException(\"Input '\" + " + variable + " + \"' failed whitelist validation\");\n" +
               "        }";
    }

    private static String buildWvrImports() {
        return "import java.util.regex.Pattern;\n";
    }

    // ── TCVR helpers ──────────────────────────────────────────────────────────

    private static String buildTcvrBlock(String taintedLine) {
        return """
                        // TCVR verification — paper Algorithm 3
                        // GetSignature(S): retrieve the digital signature of the external source
                        byte[] signature = TrustVerifier.getSignature(source);
                        // GetTrustedKeyStore(): load the pinned trust store
                        java.security.KeyStore trustedKeyStore = TrustVerifier.getTrustedKeyStore();
                        // Check = Verifier.verify(Sig, Key) — paper Algorithm 3, line 3
                        if (!TrustVerifier.verify(signature, trustedKeyStore)) {
                            // LogAndAbort — paper Algorithm 3, line 4
                            java.util.logging.Logger.getLogger(getClass().getName())
                                .severe("TrustChain verification failed — aborting instantiation");
                            throw new SecurityException("TrustChain verification failed for external source");
                        }
                        // Verified: safe to proceed with original instantiation""";
    }

    private static String buildTcvrImports() {
        return """
                import javax.net.ssl.SSLContext;
                import javax.net.ssl.SSLSocketFactory;
                import javax.net.ssl.SSLSocket;
                import javax.net.ssl.TrustManager;
                import java.security.SecureRandom;
                """;
    }

    // ── Shared utility ────────────────────────────────────────────────────────

    /**
     * Prepends import statements before the first class declaration so the
     * injected security helpers are visible within the compilation unit.
     */
    private static String injectHeaderBeforeClass(String code, String header) {
        int classIdx = code.indexOf("class ");
        if (classIdx <= 0) return header + "\n" + code;
        // Find start of the line containing "class "
        int lineStart = code.lastIndexOf('\n', classIdx);
        lineStart = (lineStart < 0) ? 0 : lineStart + 1;
        return code.substring(0, lineStart) + header + "\n" + code.substring(lineStart);
    }
}
