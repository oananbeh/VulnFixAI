<div align="center">

# 🛡️ VulnFixAI

[![Java](https://img.shields.io/badge/Java-17-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-61DAFB.svg?logo=react)](https://reactjs.org/)
[![Ollama](https://img.shields.io/badge/Ollama-AI%20Runtime-orange.svg)](https://ollama.com)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg?logo=python)](https://python.org)

### This repository is the replication package of "VulnFixAI: Automated Vulnerability Detection and fix"
</div>

---

## 🗂️ Repository Structure

- **Training DataSet**: contain the training dataset used in this study. 
- **Evaluation-Benchmark**: contain the evaluation benchmark used in this study.
- **Apply Refactoring Technique**: Contains:
  1. The implementation of refactoring techniques (TCVR, OSR, and WVR).
  2. Converts the dataset into Alpaca format.

---


## How to Run the VulnFixAI Tool

### Step 1: Install and Run Ollama

1. You can download Ollama from [ollama.com](https://ollama.com).

2. **Verify Installation**: Check if Ollama is installed by running:
   ```bash
   ollama --version
#### Running Ollama

1. **Open the terminal**: run.
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

1. Open a terminal in the root of frontEnd.
2. Run the following command:
   ```bash
   npm start
# Citation 
```bash

---

## 🎓 Citation

If you use VulnFixAI in your research, please cite our paper:

```bibtex
@article{ananbeh2025vulnfixai,
  title={VulnFixAI: A Novel LLM-Based Approach for Automated Vulnerability Detection and Repair},
  author={Obieda Ananbeh, Wala Alnozmai, and Dae-Kyoo Kima},
  journal={},
  year={2025},
  publisher={}
}
```

