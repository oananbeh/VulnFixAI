# Supplementary Appendix — VulnFixAI

This document contains the appendix material referenced in the paper *VulnFixAI: A Neuro-Symbolic Framework for Automated Vulnerability Repair*. It was moved from the manuscript to this replication package to keep the paper within page limits. Section and table numbering below follows the order in which the material is cited in the paper.

**Contents**

- [A. Open Source Projects Used in Evaluation](#a-open-source-projects-used-in-evaluation)
- [B. Detailed Breakdown of Unfixed Vulnerabilities](#b-detailed-breakdown-of-unfixed-vulnerabilities)
- [C. Identification and Fix Distribution by CWE Types](#c-identification-and-fix-distribution-by-cwe-types)
- [D. Identification and Fix Distribution Across Projects and CWE Types](#d-identification-and-fix-distribution-across-projects-and-cwe-types)

---

## A. Open Source Projects Used in Evaluation

The twenty benchmark projects were selected for their security-critical relevance, active maintenance, and diversity (66 to 16,850 lines of code). Each is tied to confirmed CVE entries validated by GitHub's Security Advisory team. None of these projects appear in the training corpus.

**Table A0. Open Source Projects Used in Evaluation**

| Project | CVE-ID | CWE-ID | Description | Affected Versions |
|---|---|---|---|---|
| eclipse-vertx | CVE-2019-17640 | CWE-22, CWE-23 | Vert.x core offers low-level, event-driven, non-blocking functionalities like HTTP, TCP, and file system access for building reactive applications. | >= 3.0.0, < 3.9.4 |
| Apache Flink | CVE-2020-17518 | CWE-22, CWE-23 | Apache Flink is an open-source framework for real-time stream and batch data processing. | >= 1.5.1, < 1.11.3 |
| OpenTSDB | CVE-2020-35476 | CWE-78 | OpenTSDB is a distributed time series database built on HBase, designed for storing and serving large-scale metrics. | <= 2.4.0 |
| Apache Hadoop | CVE-2022-25168 | CWE-78, CWE-88 | Apache Hadoop enables distributed processing of large data sets across clusters using the MapReduce programming model and HDFS for storage. | >= 2.0.0, < 2.10.2; >= 3.0.0-alpha, < 3.2.4; >= 3.3.0, < 3.3.3 |
| Netty | CVE-2022-41915 | CWE-113, CWE-436 | Netty is a Java-based asynchronous event-driven network application framework for rapid development of high-performance protocol servers and clients. | >= 4.1.83.Final, < 4.1.86.Final |
| Undertow | CVE-2018-1067 | CWE-113 | Undertow is a flexible Java web server offering blocking and non-blocking APIs, enabling composable architectures and full servlet 4.0 support. | <= 7.1.1.GA |
| Wire | CVE-2021-41193 | CWE-134 | Wire is a secure messaging platform; this CVE relates to its use of pre-built Google WebRTC binaries in AVS. | (see advisory) |
| Apache Dubbo | CVE-2021-36161 | CWE-134 | Apache Dubbo is a high-performance Java-based RPC framework offering microservices communication and service governance. | < 7.1.12 |
| Apache NiFi | CVE-2018-17195 | CWE-319, CWE-863 | Apache NiFi automates data flow between systems, providing a user-friendly interface for data ingestion, routing, and transformation. | (see advisory) |
| Apache James | CVE-2022-45935 | CWE-200, CWE-319, CWE-668 | Apache James is a modular Java-based mail server platform, allowing custom email solutions with extensible components. | >= 1.0.0, <= 1.7.1 |
| Infinispan | CVE-2019-10174 | CWE-470 | Infinispan is an open-source in-memory key/value data store and cache, offering high availability and scalability. | <= 8.2.11.Final; >= 9.0.0.Final, <= 9.4.16.Final |
| HyperSQL | CVE-2022-41853 | CWE-470 | HyperSQL Database (HSQLDB) is a relational database management system written in Java, offering in-memory and disk-based tables. | < 2.7.1 |
| Apache Hive | CVE-2022-41137 | CWE-502 | Apache Hive provides SQL-like querying for large datasets stored in Hadoop, facilitating data warehousing and analysis. | 4.0.0-alpha-1 |
| OpenMeetings | CVE-2024-54676 | CWE-502 | OpenMeetings is an Apache-licensed platform for video conferencing, instant messaging, and collaborative document editing. | >= 2.1.0, < 8.0.0 |
| Eclipse GlassFish | CVE-2024-9329 | CWE-233, CWE-601 | Eclipse GlassFish is a Jakarta EE-compliant application server, offering implementations of all Jakarta EE APIs. | (see advisory) |
| Keycloak | CVE-2024-8883 | CWE-601 | Keycloak is an open-source identity and access management solution, supporting single sign-on with OAuth2, OpenID Connect, and SAML. | < 7.0.17 |
| HugeGraph-Toolchain | CVE-2024-27347 | CWE-918 | HugeGraph-Toolchain integrates utilities for HugeGraph, including over five main modules for enhanced graph data management. | <= 22.0.12; >= 23.0.0, <= 24.0.7; >= 25.0.0, <= 25.0.5 |
| Apache CXF | CVE-2024-28752 | CWE-918 | Apache CXF is an open-source services framework facilitating the development of SOAP and RESTful web services. | (see advisory) |
| FitNesse | CVE-2024-39610 | CWE-79 | FitNesse is a web server-based tool for specifying and verifying application acceptance criteria through collaborative testing. | < 3.5.8; >= 3.6.0, < 3.6.3; >= 4.0.0, < 4.0.4 |
| OpenRefine | CVE-2024-47882 | CWE-79, CWE-81 | OpenRefine is a Java-based tool for cleaning and transforming messy data, enhancing data quality and consistency. | < 3.8 |

---

## B. Detailed Breakdown of Unfixed Vulnerabilities

This section provides the detailed project- and category-level distribution of the 218 unfixed vulnerabilities analyzed in the paper's failure analysis (Section on Failure Analysis). Table A1 details the distribution across the 16 affected projects, highlighting the concentration in testing-heavy frameworks such as Apache Hadoop and Flink. Table A2 breaks down these instances by vulnerability category and CWE ID, demonstrating the prevalence of LOIS and ITV failures in test environments.

**Table B1. Unfixed Vulnerabilities by Project**

| Project Name | ITV | LOWV | LOIS | Total Unfixed |
|---|---|---|---|---|
| Apache CXF | 8 | 8 | 2 | 18 |
| Apache Dubbo | 2 | 0 | 2 | 4 |
| Apache Flink | 18 | 19 | 1 | 38 |
| Apache Hadoop | 0 | 24 | 30 | 54 |
| Apache Hive | 10 | 7 | 12 | 29 |
| Apache NiFi | 8 | 4 | 2 | 14 |
| Eclipse GlassFish | 0 | 0 | 4 | 4 |
| FitNesse | 0 | 0 | 1 | 1 |
| Hugegraph-toolchain | 1 | 0 | 2 | 3 |
| Infinispan | 4 | 1 | 4 | 9 |
| James | 6 | 0 | 0 | 6 |
| Keycloak | 2 | 2 | 1 | 5 |
| eclipse-vertx | 0 | 0 | 1 | 1 |
| netty | 6 | 0 | 5 | 11 |
| openmeetings | 2 | 0 | 1 | 3 |
| undertow | 14 | 1 | 3 | 18 |

**Table B2. Unfixed Vulnerabilities by Category**

| Vulnerability Category | Count | Percentage | CWE IDs |
|---|---|---|---|
| ITV | 80 | 36.70% | CWE-295, CWE-296, CWE-275, CWE-297, CWE-298, CWE-299, CWE-599, CWE-319, CWE-320, CWE-321, CWE-326, CWE-327, CWE-330, CWE-329, CWE-353, CWE-345, CWE-346, CWE-347, CWE-358, CWE-775, CWE-401, CWE-494, CWE-502, CWE-522, CWE-532, CWE-547, CWE-668, CWE-732, CWE-916, CWE-918, CWE-940, CWE-941, CWE-1021 |
| LOWV | 55 | 25.20% | CWE-20, CWE-23, CWE-36, CWE-41, CWE-59, CWE-74, CWE-77, CWE-119, CWE-120, CWE-130, CWE-131, CWE-134, CWE-179, CWE-180, CWE-181, CWE-182, CWE-183, CWE-184, CWE-185, CWE-186, CWE-208, CWE-306, CWE-354, CWE-434, CWE-444, CWE-470, CWE-501, CWE-601, CWE-611, CWE-641, CWE-707, CWE-787, CWE-943, CWE-1173, CWE-1919 |
| LOIS | 83 | 38.10% | CWE-74, CWE-75, CWE-76, CWE-77, CWE-78, CWE-79, CWE-80, CWE-83, CWE-85, CWE-86, CWE-88, CWE-89, CWE-90, CWE-91, CWE-93, CWE-94, CWE-95, CWE-96, CWE-97, CWE-98, CWE-99, CWE-113, CWE-116, CWE-117, CWE-134, CWE-138, CWE-140, CWE-150, CWE-151, CWE-152, CWE-157, CWE-158, CWE-159, CWE-160, CWE-165, CWE-166, CWE-167, CWE-176, CWE-352, CWE-434, CWE-470, CWE-471, CWE-502, CWE-564, CWE-601, CWE-614, CWE-642, CWE-643, CWE-917 |

---

## C. Identification and Fix Distribution by CWE Types

**Table C1. Identification distribution by CWE types**

| CWE ID | VulnFixAI | ChatGPT 5.2 | Claude 4.5 Sonnet | Gemini 3.0 Flash | llama3.2 (3B) |
|---|---|---|---|---|---|
| CWE-23 | 499 | 421 | 414 | 413 | 392 |
| CWE-319 | 375 | 353 | 350 | 331 | 208 |
| CWE-470 | 191 | 179 | 174 | 174 | 175 |
| CWE-918 | 187 | 164 | 159 | 139 | 130 |
| CWE-79 | 138 | 128 | 125 | 120 | 116 |
| CWE-611 | 95 | 83 | 82 | 81 | 79 |
| CWE-916 | 78 | 66 | 55 | 55 | 56 |
| CWE-113 | 69 | 51 | 51 | 50 | 55 |
| CWE-295 | 43 | 33 | 31 | 31 | 30 |
| CWE-89 | 41 | 29 | 29 | 28 | 33 |
| CWE-78 | 32 | 26 | 26 | 24 | 25 |
| CWE-614 | 28 | 20 | 20 | 20 | 23 |
| CWE-601 | 26 | 18 | 18 | 18 | 20 |
| CWE-352 | 21 | 15 | 15 | 15 | 16 |
| CWE-502 | 13 | 9 | 9 | 9 | 12 |
| CWE-134 | 12 | 9 | 9 | 9 | 10 |
| CWE-208 | 11 | 9 | 9 | 7 | 7 |
| CWE-732 | 11 | 8 | 8 | 8 | 8 |
| CWE-326 | 10 | 9 | 8 | 8 | 7 |
| CWE-327 | 8 | 7 | 5 | 5 | 5 |
| CWE-501 | 4 | 3 | 3 | 3 | 3 |
| CWE-90 | 3 | 2 | 2 | 2 | 2 |
| CWE-547 | 2 | 2 | 2 | 2 | 2 |

**Table C2. Fix distribution by CWE types**

| CWE ID | VulnFixAI | ChatGPT 5.2 | Claude 4.5 Sonnet | Gemini 3.0 Flash | llama3.2 (3B) |
|---|---|---|---|---|---|
| CWE-23 | 461 | 356 | 343 | 337 | 289 |
| CWE-319 | 357 | 311 | 287 | 266 | 234 |
| CWE-918 | 179 | 140 | 134 | 112 | 99 |
| CWE-470 | 171 | 146 | 137 | 135 | 118 |
| CWE-79 | 133 | 109 | 105 | 101 | 79 |
| CWE-611 | 86 | 71 | 68 | 68 | 62 |
| CWE-113 | 66 | 43 | 43 | 42 | 38 |
| CWE-916 | 52 | 50 | 48 | 48 | 43 |
| CWE-295 | 39 | 28 | 27 | 26 | 24 |
| CWE-78 | 31 | 21 | 20 | 20 | 18 |
| CWE-601 | 26 | 16 | 16 | 15 | 15 |
| CWE-614 | 22 | 17 | 17 | 17 | 16 |
| CWE-352 | 19 | 13 | 12 | 12 | 11 |
| CWE-89 | 16 | 26 | 26 | 25 | 23 |
| CWE-134 | 12 | 8 | 8 | 8 | 8 |
| CWE-208 | 11 | 7 | 7 | 7 | 7 |
| CWE-326 | 9 | 7 | 7 | 7 | 7 |
| CWE-502 | 8 | 9 | 9 | 9 | 9 |
| CWE-327 | 5 | 5 | 5 | 5 | 5 |
| CWE-501 | 4 | 3 | 2 | 2 | 2 |
| CWE-90 | 3 | 2 | 2 | 2 | 2 |
| CWE-547 | 1 | 2 | 2 | 2 | 2 |
| CWE-732 | 0 | 7 | 7 | 7 | 6 |

---

## D. Identification and Fix Distribution Across Projects and CWE Types

**Table D1. Identification results of top 5 CWE types across projects**

| Project Name | CWE ID | VulnFixAI | ChatGPT 5.2 | Claude 4.5 Sonnet | Gemini 3.0 Flash | llama3.2 (3B) |
|---|---|---|---|---|---|---|
| Eclipse GlassFish | CWE-23 | 187 | 181 | 178 | 180 | 166 |
| Apache NiFi | CWE-23 | 56 | 43 | 42 | 42 | 41 |
| Apache Hadoop | CWE-23 | 51 | 39 | 38 | 38 | 37 |
| Apache CXF | CWE-23 | 50 | 38 | 38 | 37 | 36 |
| Apache Flink | CWE-23 | 28 | 21 | 21 | 21 | 20 |
| Eclipse GlassFish | CWE-319 | 298 | 292 | 290 | 276 | 157 |
| Apache NiFi | CWE-319 | 35 | 27 | 31 | 26 | 24 |
| James | CWE-319 | 9 | 7 | 7 | 7 | 6 |
| undertow | CWE-319 | 9 | 7 | 7 | 7 | 6 |
| Apache Flink | CWE-319 | 6 | 5 | 4 | 4 | 4 |
| Apache Hadoop | CWE-470 | 152 | 149 | 144 | 144 | 145 |
| Apache Hadoop | CWE-470 | 15 | 11 | 11 | 11 | 11 |
| Eclipse GlassFish | CWE-470 | 8 | 6 | 6 | 6 | 6 |
| HyperSQL | CWE-470 | 5 | 4 | 4 | 4 | 4 |
| Eclipse GlassFish | CWE-470 | 4 | 3 | 3 | 3 | 3 |
| Eclipse GlassFish | CWE-918 | 121 | 112 | 110 | 90 | 84 |
| Apache CXF | CWE-918 | 23 | 17 | 17 | 17 | 16 |
| Apache Hadoop | CWE-918 | 16 | 12 | 12 | 12 | 11 |
| Keycloak | CWE-918 | 12 | 9 | 9 | 9 | 8 |
| Apache CXF | CWE-918 | 4 | 3 | 3 | 3 | 3 |
| Eclipse GlassFish | CWE-79 | 108 | 106 | 104 | 99 | 92 |
| openmeetings | CWE-79 | 12 | 9 | 9 | 9 | 9 |
| Keycloak | CWE-79 | 7 | 5 | 5 | 5 | 5 |
| Apache Dubbo | CWE-79 | 2 | 1 | 1 | 1 | 2 |
| FitNesse | CWE-79 | 2 | 2 | 1 | 1 | 1 |

**Table D2. Fix results of top 5 CWE types across projects**

| Project Name | CWE ID | VulnFixAI | llama3.2 (3B) | ChatGPT 5.2 | Claude 4.5 Sonnet | Gemini 3.0 Flash |
|---|---|---|---|---|---|---|
| Eclipse GlassFish | CWE-23 | 187 | 121 | 157 | 153 | 150 |
| Apache NiFi | CWE-23 | 56 | 30 | 36 | 34 | 33 |
| Apache Hadoop | CWE-23 | 51 | 27 | 32 | 31 | 30 |
| Apache CXF | CWE-23 | 50 | 26 | 32 | 31 | 30 |
| Apache Flink | CWE-23 | 28 | 15 | 18 | 17 | 17 |
| Eclipse GlassFish | CWE-319 | 298 | 193 | 262 | 237 | 221 |
| Apache NiFi | CWE-319 | 35 | 18 | 22 | 26 | 21 |
| James | CWE-319 | 9 | 5 | 6 | 5 | 5 |
| Apache Flink | CWE-319 | 6 | 3 | 4 | 4 | 4 |
| FitNesse | CWE-319 | 4 | 2 | 3 | 2 | 2 |
| Eclipse GlassFish | CWE-918 | 121 | 63 | 96 | 93 | 72 |
| Apache CXF | CWE-918 | 23 | 12 | 15 | 14 | 14 |
| Apache Hadoop | CWE-918 | 16 | 8 | 10 | 10 | 9 |
| Keycloak | CWE-918 | 12 | 6 | 8 | 7 | 7 |
| Hugegraph-toolchain | CWE-918 | 2 | 1 | 1 | 1 | 1 |
| Apache Hadoop | CWE-470 | 152 | 95 | 121 | 112 | 110 |
| Eclipse GlassFish | CWE-470 | 8 | 4 | 5 | 5 | 5 |
| HyperSQL | CWE-470 | 5 | 3 | 3 | 3 | 3 |
| Keycloak | CWE-470 | 3 | 2 | 2 | 2 | 2 |
| Apache CXF | CWE-470 | 1 | 1 | 1 | 1 | 1 |
| Eclipse GlassFish | CWE-79 | 108 | 64 | 92 | 89 | 83 |
| openmeetings | CWE-79 | 12 | 6 | 7 | 7 | 7 |
| Keycloak | CWE-79 | 7 | 4 | 4 | 4 | 4 |
| FitNesse | CWE-79 | 2 | 0 | 1 | 0 | 1 |
| OpenRefine | CWE-79 | 2 | 1 | 1 | 1 | 1 |
