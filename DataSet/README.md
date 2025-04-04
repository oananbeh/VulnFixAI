# Dataset Explanation

### `dataset.csv`
- **Purpose**: This is the raw dataset containing rows of code snippets and their associated vulnerabilities.
- **Columns**:
  - `CWE ID`: The Common Weakness Enumeration (CWE) identifier for the vulnerability.
  - `Code Snippet`: The code snippet containing the vulnerability.
  - `Exact Vulnerable Line`: The specific line in the code snippet where the vulnerability occurs.
  - `Description`: A brief explanation of the vulnerability.

### `dataSet_withFixes.csv`: This dataset includes fixes for the vulnerabilities after applying refactoring techniques.

### `VulnFixAI_dataset.csv` : This dataset is in Alpaca format and is used for fine-tuning the VulnFixAI model.

