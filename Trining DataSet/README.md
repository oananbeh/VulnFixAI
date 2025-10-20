# Training Dataset Explanation

This directory contains the datasets used for training the VulnFixAI model, which is designed to detect and fix security vulnerabilities in Java code.

## Dataset Files

### `dataset.csv`
- **Purpose**: Raw dataset containing code snippets with associated vulnerabilities and benign code examples.
- **Size**: 20000 rows (10000 benign, "vulnerable")
- **Format**: CSV
- **Columns**:
  - `CWE ID`: The Common Weakness Enumeration (CWE) identifier for the vulnerability, or "BENIGN" for non-vulnerable code
  - `Project Name`: The source project from which the code snippet was extracted
  - `Vulnerable File`: Path to the file containing the vulnerability
  - `Programming Language`: Programming language of the code snippet (Java)
  - `Line Number`: Line number where the vulnerability occurs in the original file
  - `Code Snippet`: The complete code snippet context containing the vulnerability
  - `Exact Vulnerable Line`: The specific line in the code snippet where the vulnerability occurs
  - `Description`: A brief explanation of the vulnerability or "Benign code snippet" for safe code
  - `Status`: Classification status ("benign", "vulnerable")

### `dataSet_withFixes.csv`
- **Purpose**: This dataset includes fixes for the vulnerabilities after applying refactoring techniques (TCVR, OSR, and WVR)

### `VulnFixAI_dataset.csv`
- **Purpose**: Fine-tuning dataset formatted in Alpaca format specifically for the VulnFixAI
- **Format**: Alpaca format (instruction-input-output structure)
- **Structure**: Each entry contains:
  - Instruction for the model
  - Input (vulnerable code snippet)
  - Output (fixed code or vulnerability analysis)
- **Use Case**: Fine-tuning large language models for vulnerability detection and automatic code fixing

## Notes

- The dataset focuses on Java security vulnerabilities
- Both vulnerable and benign code samples are included for balanced training
- CWE standards are used for vulnerability classification
- Refactoring techniques have been applied to generate secure code variants