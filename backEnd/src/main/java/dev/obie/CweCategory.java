package dev.obie;

/**
 * Vulnerability categories that map to deterministic refactoring templates.
 *
 * Paper reference: Section IV-D (Algorithmic Specifications) and Table I.
 * Each category corresponds to one Symbolic Enforcer template:
 *   LOWV → WVR  (Whitelist Validation Refactoring — Algorithm 2)
 *   LOIS → OSR  (Output Safety Refactoring — Algorithm 1)
 *   ITV  → TCVR (TrustChain Verification Refactoring — Algorithm 3)
 *   UNKNOWN → Fallback (Iterative Neuro-Refinement — Algorithm 4)
 *
 * @author Obieda Ananbeh
 */
public enum CweCategory {
    /** Lack of Whitelist Validation — remediated by WVR */
    LOWV,
    /** Lack of Input Sanitization — remediated by OSR */
    LOIS,
    /** Inadequate TrustChain Verification — remediated by TCVR */
    ITV,
    /** CWE does not map to any of the three primary templates; use Fallback */
    UNKNOWN
}
