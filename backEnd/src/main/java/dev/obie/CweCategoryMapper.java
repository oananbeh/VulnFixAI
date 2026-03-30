package dev.obie;

import java.util.Map;

/**
 * Maps CWE identifiers to the three vulnerability categories used by the
 * Symbolic Enforcer, exactly as defined in Table I of the paper.
 *
 * Paper reference: Section IV-D, Table I — "Mapping of CWE Identifiers to
 * Refactoring Templates (The Selection Logic for E)".
 *
 * Where a CWE appears in multiple categories (e.g. CWE-134 in both LOIS and
 * LOWV), the assignment follows the ordering used in the training dataset
 * (apply_refactoring.ipynb, cell-0).
 *
 * @author Obieda Ananbeh
 */
public final class CweCategoryMapper {

    private CweCategoryMapper() {}

    /**
     * Full CWE → category mapping derived from Table I.
     * LOIS entries take priority for CWEs that appear in both LOIS and LOWV.
     */
    private static final Map<String, CweCategory> CWE_MAP = Map.ofEntries(

        // ── LOIS (Lack of Input Sanitization → OSR) ──────────────────────────
        // Paper Table I, row 1 — partial list of supported CWEs
        Map.entry("CWE-74",  CweCategory.LOIS),
        Map.entry("CWE-75",  CweCategory.LOIS),
        Map.entry("CWE-76",  CweCategory.LOIS),
        Map.entry("CWE-77",  CweCategory.LOIS),
        Map.entry("CWE-78",  CweCategory.LOIS),
        Map.entry("CWE-79",  CweCategory.LOIS),
        Map.entry("CWE-80",  CweCategory.LOIS),
        Map.entry("CWE-83",  CweCategory.LOIS),
        Map.entry("CWE-85",  CweCategory.LOIS),
        Map.entry("CWE-86",  CweCategory.LOIS),
        Map.entry("CWE-88",  CweCategory.LOIS),
        Map.entry("CWE-89",  CweCategory.LOIS),
        Map.entry("CWE-90",  CweCategory.LOIS),
        Map.entry("CWE-91",  CweCategory.LOIS),
        Map.entry("CWE-93",  CweCategory.LOIS),
        Map.entry("CWE-94",  CweCategory.LOIS),
        Map.entry("CWE-95",  CweCategory.LOIS),
        Map.entry("CWE-96",  CweCategory.LOIS),
        Map.entry("CWE-97",  CweCategory.LOIS),
        Map.entry("CWE-98",  CweCategory.LOIS),
        Map.entry("CWE-99",  CweCategory.LOIS),
        Map.entry("CWE-113", CweCategory.LOIS),
        Map.entry("CWE-116", CweCategory.LOIS),
        Map.entry("CWE-117", CweCategory.LOIS),
        Map.entry("CWE-134", CweCategory.LOIS),
        Map.entry("CWE-138", CweCategory.LOIS),
        Map.entry("CWE-140", CweCategory.LOIS),
        Map.entry("CWE-150", CweCategory.LOIS),
        Map.entry("CWE-151", CweCategory.LOIS),
        Map.entry("CWE-152", CweCategory.LOIS),
        Map.entry("CWE-157", CweCategory.LOIS),
        Map.entry("CWE-158", CweCategory.LOIS),
        Map.entry("CWE-159", CweCategory.LOIS),
        Map.entry("CWE-160", CweCategory.LOIS),
        Map.entry("CWE-165", CweCategory.LOIS),
        Map.entry("CWE-166", CweCategory.LOIS),
        Map.entry("CWE-167", CweCategory.LOIS),
        Map.entry("CWE-176", CweCategory.LOIS),
        Map.entry("CWE-352", CweCategory.LOIS),
        Map.entry("CWE-434", CweCategory.LOIS),
        Map.entry("CWE-470", CweCategory.LOIS),
        Map.entry("CWE-471", CweCategory.LOIS),
        Map.entry("CWE-502", CweCategory.LOIS),
        Map.entry("CWE-564", CweCategory.LOIS),
        Map.entry("CWE-601", CweCategory.LOIS),
        Map.entry("CWE-614", CweCategory.LOIS),
        Map.entry("CWE-642", CweCategory.LOIS),
        Map.entry("CWE-643", CweCategory.LOIS),
        Map.entry("CWE-917", CweCategory.LOIS)
    );

    // LOWV and ITV are in a separate map to stay within Map.ofEntries limit
    private static final Map<String, CweCategory> CWE_MAP_2 = Map.ofEntries(

        // ── LOWV (Lack of Whitelist Validation → WVR) ──────────────────────
        // Paper Table I, row 2 — CWEs not already claimed by LOIS above
        Map.entry("CWE-20",   CweCategory.LOWV),
        Map.entry("CWE-23",   CweCategory.LOWV),
        Map.entry("CWE-36",   CweCategory.LOWV),
        Map.entry("CWE-41",   CweCategory.LOWV),
        Map.entry("CWE-59",   CweCategory.LOWV),
        Map.entry("CWE-119",  CweCategory.LOWV),
        Map.entry("CWE-120",  CweCategory.LOWV),
        Map.entry("CWE-130",  CweCategory.LOWV),
        Map.entry("CWE-131",  CweCategory.LOWV),
        Map.entry("CWE-179",  CweCategory.LOWV),
        Map.entry("CWE-180",  CweCategory.LOWV),
        Map.entry("CWE-181",  CweCategory.LOWV),
        Map.entry("CWE-182",  CweCategory.LOWV),
        Map.entry("CWE-183",  CweCategory.LOWV),
        Map.entry("CWE-184",  CweCategory.LOWV),
        Map.entry("CWE-185",  CweCategory.LOWV),
        Map.entry("CWE-186",  CweCategory.LOWV),
        Map.entry("CWE-208",  CweCategory.LOWV),
        Map.entry("CWE-306",  CweCategory.LOWV),
        Map.entry("CWE-354",  CweCategory.LOWV),
        Map.entry("CWE-444",  CweCategory.LOWV),
        Map.entry("CWE-501",  CweCategory.LOWV),
        Map.entry("CWE-611",  CweCategory.LOWV),
        Map.entry("CWE-641",  CweCategory.LOWV),
        Map.entry("CWE-707",  CweCategory.LOWV),
        Map.entry("CWE-787",  CweCategory.LOWV),
        Map.entry("CWE-943",  CweCategory.LOWV),
        Map.entry("CWE-1173", CweCategory.LOWV),
        Map.entry("CWE-1919", CweCategory.LOWV),

        // ── ITV (Inadequate TrustChain Verification → TCVR) ─────────────────
        // Paper Table I, row 3
        Map.entry("CWE-275",  CweCategory.ITV),
        Map.entry("CWE-295",  CweCategory.ITV),
        Map.entry("CWE-296",  CweCategory.ITV),
        Map.entry("CWE-297",  CweCategory.ITV),
        Map.entry("CWE-298",  CweCategory.ITV),
        Map.entry("CWE-299",  CweCategory.ITV),
        Map.entry("CWE-319",  CweCategory.ITV),
        Map.entry("CWE-320",  CweCategory.ITV),
        Map.entry("CWE-321",  CweCategory.ITV),
        Map.entry("CWE-326",  CweCategory.ITV),
        Map.entry("CWE-327",  CweCategory.ITV),
        Map.entry("CWE-329",  CweCategory.ITV),
        Map.entry("CWE-330",  CweCategory.ITV),
        Map.entry("CWE-345",  CweCategory.ITV),
        Map.entry("CWE-346",  CweCategory.ITV),
        Map.entry("CWE-347",  CweCategory.ITV),
        Map.entry("CWE-353",  CweCategory.ITV),
        Map.entry("CWE-358",  CweCategory.ITV),
        Map.entry("CWE-401",  CweCategory.ITV),
        Map.entry("CWE-494",  CweCategory.ITV),
        Map.entry("CWE-522",  CweCategory.ITV),
        Map.entry("CWE-532",  CweCategory.ITV),
        Map.entry("CWE-547",  CweCategory.ITV),
        Map.entry("CWE-599",  CweCategory.ITV),
        Map.entry("CWE-668",  CweCategory.ITV),
        Map.entry("CWE-732",  CweCategory.ITV),
        Map.entry("CWE-775",  CweCategory.ITV),
        Map.entry("CWE-916",  CweCategory.ITV),
        Map.entry("CWE-918",  CweCategory.ITV),
        Map.entry("CWE-940",  CweCategory.ITV),
        Map.entry("CWE-941",  CweCategory.ITV),
        Map.entry("CWE-1021", CweCategory.ITV)
    );

    /**
     * Returns the vulnerability category for a given CWE identifier string.
     *
     * @param cweId e.g. "CWE-79" or "79"
     * @return the matching category, or {@link CweCategory#UNKNOWN} if not found
     */
    public static CweCategory map(String cweId) {
        if (cweId == null || cweId.isBlank()) return CweCategory.UNKNOWN;

        // Normalise: accept both "CWE-79" and "79"
        String key = cweId.trim().toUpperCase();
        if (!key.startsWith("CWE-")) key = "CWE-" + key;

        CweCategory cat = CWE_MAP.get(key);
        if (cat == null) cat = CWE_MAP_2.get(key);
        return cat != null ? cat : CweCategory.UNKNOWN;
    }
}
