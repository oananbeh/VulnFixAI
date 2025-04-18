# VulnFixAI
This repository is the replication package of "VulnFixAI: Automated Vulnerability Detection and fix" 

- **DataSet Folder**: contain the dataSet use in this study. 

- **Apply Refactoring Technique**: Contains the implementation of refactoring techniques (TCVR, OSR, and WVR) and also converts the dataset into Alpaca format.


## How to Run the VulnFixAI Tool

### 1: Install and Run Ollama

1. Make sure you have Python installed on your system. Ollama requires Python 3.7 or higher. You can download Python from [python.org](https://www.python.org/).
2. Install the required dependencies for Ollama using pip. Run the following command in your terminal or command prompt:
   ```bash
   pip install ollama
3. **Verify Installation**: Check if Ollama is installed by running:
   ```bash
   ollama --version
#### Running Ollama

1. **Navigate to Scripts**: Go to the directory containing the scripts or notebooks you want to run.
2. **Run Ollama**: Execute a script using:
   ```bash
   ollama run <script_name>.py
