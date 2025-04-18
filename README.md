# VulnFixAI
This repository is the replication package of "VulnFixAI: Automated Vulnerability Detection and fix" 

- **DataSet Folder**: contain the dataSet use in this study. 

- **Apply Refactoring Technique**: Contains:
  1. The implementation of refactoring techniques (TCVR, OSR, and WVR).
  2. Converts the dataset into Alpaca format.


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
  ollama run <Model Name>   
## Step 2: Run the Backend

1. Open the backend in the intellij IDE.
2. Change the `application.properties` file and update the following values:
   ```properties
   # Ollama Configuration
   spring.ai.ollama.base-url=${Ollama-URL}// use http://Localhost:11434  if you run it locally
   spring.ai.ollama.chat.model=${model} // add the model
   
## Step 3: Run the Frontend

1. Open a terminal in the root of your project.
2. Run the following command:
   ```bash
   npm start
