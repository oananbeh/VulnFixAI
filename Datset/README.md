# Dataset Explanation

This directory contains datasets used for vulnerability detection and fixing, as well as for fine-tuning the VulnFixAI model. Below is an explanation of the datasets and their columns:

## Files

### `dataset.csv`
- **Purpose**: This is the raw dataset containing rows of code snippets and their associated vulnerabilities.
- **Columns**:
  - `CWE ID`: The Common Weakness Enumeration (CWE) identifier for the vulnerability.
  - `Code Snippet`: The code snippet containing the vulnerability.
  - `Exact Vulnerable Line`: The specific line in the code snippet where the vulnerability occurs.
  - `Description`: A brief explanation of the vulnerability.

### `dataSet_withFixes.csv`
- **Purpose**: This dataset includes fixes for the vulnerabilities after applying refactoring techniques.
- **Columns**:
  - `CWE ID`: The CWE identifier for the vulnerability.
  - `Code Snippet`: The code snippet containing the vulnerability.
  - `Exact Vulnerable Line`: The specific line in the code snippet where the vulnerability occurs.
  - `Description`: A brief explanation of the vulnerability.
  - `code_fix`: The fixed version of the code snippet after applying refactoring techniques such as:
    - **TCVR**: Technique-Centric Vulnerability Refactoring.
    - **OSR**: Object-Specific Refactoring.
    - **WVR**: Workflow Vulnerability Refactoring.

### `VulnFixAI_dataset.csv`
- **Purpose**: This dataset is in Alpaca format and is used for fine-tuning the VulnFixAI model.
- **Columns**:
  - `instruction`: The task instruction for the model, such as identifying or fixing vulnerabilities.
  - `input`: The code snippet provided to the model.
  - `output`: The expected output from the model, such as the exact vulnerable line or the fixed code snippet.

## Usage
- Use `dataset.csv` for analyzing raw vulnerabilities.
- Use `dataSet_withFixes.csv` for studying refactored fixes.
- Use `VulnFixAI_dataset.csv` for fine-tuning the VulnFixAI model to detect and fix vulnerabilities.
