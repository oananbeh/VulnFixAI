<div align="center">

# VulnFixAI

[![Java](https://img.shields.io/badge/Java-17-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-61DAFB.svg?logo=react)](https://reactjs.org/)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg?logo=python)](https://python.org)
[![Llama](https://img.shields.io/badge/Llama_3.2-3B-orange.svg)](https://ollama.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

### Replication Package for "A Neuro-Symbolic Framework for Automated Vulnerability Detection and Repair in Java Software"

*Obieda Ananbeh · Wala Alnozmai · Dae-Kyoo Kim*

</div>

---

## Overview

VulnFixAI is a neuro-symbolic framework that combines a fine-tuned **Neural Oracle** (Llama 3.2 3B) with a deterministic **Symbolic Enforcer** to detect and repair software vulnerabilities in Java code. The framework targets three vulnerability categories — Lack of Whitelist Validation (LOWV), Lack of Input Sanitization (LOIS), and Inadequate TrustChain Verification (ITV) — using three formally verified refactoring algorithms: WVR, OSR, and TCVR.

**Key results (10,006-sample Java benchmark, 20 open-source projects):**

- **89% overall effectiveness** (identification + repair)
- **98–99% identification rate** across all three vulnerability categories
- **+30.6 pp EM** over CodeRover-S (nearest DL SOTA competitor)
- **+15–18%** repair validity over ChatGPT-5.2
- **+51%** gain over non-fine-tuned Llama 3.2 (same base model)

---

## Repository Structure

```
VulnFixAI/
├── VulnFixAI.ipynb                  # Main training notebook (fine-tuning Llama 3.2 with LoRA/Unsloth)
├── analysis.ipynb                   # Benign sample analysis and dataset construction
├── dl_sota_comparison.ipynb         # Comparison vs. DL SOTA (CodeRover-S, VulMaster, VulRepair, VRepair)
├── sllm_comparison.ipynb            # Comparison vs. general-purpose SLLMs (DeepSeek-Coder, StarCoder-2, etc.)
├── sast_comparison.ipynb            # Comparison vs. SAST tools (SonarQube, Semgrep)
├── Results.xlsx                     # Per-project raw results (identification + fix counts per model)
│
├── Trining DataSet/                 # Training data
│   ├── VulnFixAI_dataset.csv.zip    # Full training dataset (10,000 vulnerable Java snippets)
│   ├── VulnFixAI_dataset.json.zip   # JSON format
│   ├── dataSet_withFixes.csv.zip    # Dataset with applied fixes
│   └── dataset.csv.zip              # Raw dataset
│
├── Evaluation-Benchmark/            # Evaluation data
│   ├── SVD-Benchmark.csv            # 10,006-sample Java benchmark (20 open-source projects)
│   └── README.md
│
├── Apply refactoring technique/     # Symbolic Enforcer implementation
│   ├── apply_refactoring.ipynb      # WVR, OSR, TCVR algorithms
│   ├── alpaca_format_dataset.ipynb  # Converts dataset to Alpaca format for fine-tuning
│   ├── DataSet/                     # Per-category datasets (ITV.csv, LOIS.csv, LOWV.csv)
│   └── fix_Code/                    # Datasets with applied fixes
│
├── backEnd/                         # Spring Boot backend (Ollama integration)
└── frontEnd/                        # React frontend (vulnerability scan UI)
```

---

## Research Questions

**RQ1** — To what extent does a neuro-symbolic architecture mitigate hallucination and inaccuracy in purely generative LLM approaches?

**RQ2** — Can a constrained neuro-symbolic framework outperform state-of-the-art agentic and static analysis tools on real-world Java projects?

**RQ3** — How does imposing deterministic refactoring templates affect generalization across diverse vulnerability categories (LOWV, LOIS, ITV)?

---

## Baseline Comparisons

### DL SOTA — `dl_sota_comparison.ipynb`

| Model | Repository | EM (%) | BLEU-4 (%) | CodeBLEU (%) |
|---|---|---|---|---|
| **VulnFixAI** | *(this repo)* | **89.0** | **91.2** | **93.5** |
| CodeRover-S | [nus-apr/auto-code-rover](https://github.com/nus-apr/auto-code-rover) | 58.4 | 64.1 | 68.3 |
| VulMaster | [soarsmu/VulMaster_](https://github.com/soarsmu/VulMaster_) | 51.2 | 60.5 | 65.8 |
| VulRepair | [awsm-research/VulRepair](https://github.com/awsm-research/VulRepair) | 44.0 | 53.7 | 59.1 |
| VRepair | [ASSERT-KTH/VRepair](https://github.com/ASSERT-KTH/VRepair) | 21.9 | 29.3 | 40.9 |

### SLLM Baselines — `sllm_comparison.ipynb`

| Model | HuggingFace / Repository | EM (%) | CodeBLEU (%) | Fix Rate (%) |
|---|---|---|---|---|
| **VulnFixAI (3B)** | *(this repo)* | **89.0** | **93.5** | **88.6** |
| DeepSeek-Coder (1.3B) | [deepseek-ai/deepseek-coder-1.3b-instruct](https://huggingface.co/deepseek-ai/deepseek-coder-1.3b-instruct) | 64.2 | 71.8 | 61.5 |
| StarCoder-2 (3B) | [bigcode/starcoder2-3b](https://huggingface.co/bigcode/starcoder2-3b) | 61.5 | 69.4 | 58.2 |
| Qwen2.5-Coder (1.5B) | [Qwen/Qwen2.5-Coder-1.5B-Instruct](https://huggingface.co/Qwen/Qwen2.5-Coder-1.5B-Instruct) | 60.8 | 68.1 | 57.9 |
| Llama 3.2 Base (3B) | [unsloth/Llama-3.2-3B-Instruct-bnb-4bit](https://huggingface.co/unsloth/Llama-3.2-3B-Instruct-bnb-4bit) | 59.0 | 65.2 | 54.4 |
| CodeGemma (2B) | [google/codegemma-2b](https://huggingface.co/google/codegemma-2b) | 55.3 | 62.9 | 51.1 |

### SAST Tools — `sast_comparison.ipynb`

| Tool | Repository | Precision (%) | Recall (%) | F1 (%) |
|---|---|---|---|---|
| **VulnFixAI** | *(this repo)* | **99.1** | **98.2** | **98.6** |
| Semgrep (OSS) | [semgrep/semgrep](https://github.com/semgrep/semgrep) | 84.5 | 41.2 | 55.4 |
| SonarQube (Community) | [SonarSource/sonarqube](https://github.com/SonarSource/sonarqube) | 91.2 | 14.7 | 25.3 |

---

## How to Run

### Prerequisites

- Python 3.8+
- Java 17
- Node.js 18+
- [Ollama](https://ollama.com)
- GPU with at least 8 GB VRAM (for fine-tuning; inference runs on CPU)

### Step 1 — Fine-tune the Model

Open `VulnFixAI.ipynb` and run all cells. The notebook loads Llama 3.2 3B via Unsloth (4-bit quantized), converts the training dataset to Alpaca format, runs LoRA fine-tuning, and saves the adapter to `lora_model/`.

```bash
pip install unsloth trl transformers sacrebleu codebleu
```

### Step 2 — Apply Refactoring Templates

Open `Apply refactoring technique/apply_refactoring.ipynb` to run the Symbolic Enforcer (WVR, OSR, TCVR algorithms) on the detected vulnerabilities.

### Step 3 — Run the Backend

1. Install and start Ollama, then pull your fine-tuned model:
   ```bash
   ollama run <your-model-name>
   ```

2. Open `backEnd/` in IntelliJ IDEA and update `application.properties`:
   ```properties
   spring.ai.ollama.base-url=http://localhost:11434
   spring.ai.ollama.chat.model=<your-model-name>
   ```

3. Run the Spring Boot application.

### Step 4 — Run the Frontend

```bash
cd frontEnd
npm install
npm start
```

The UI will be at `http://localhost:3000`.

---

## Reproducing Evaluations

| Evaluation | Notebook |
|---|---|
| Model fine-tuning | `VulnFixAI.ipynb` |
| DL SOTA comparison | `dl_sota_comparison.ipynb` |
| SLLM baseline comparison | `sllm_comparison.ipynb` |
| SAST tool comparison | `sast_comparison.ipynb` |
| Benign sample analysis | `analysis.ipynb` |
| Refactoring algorithm | `Apply refactoring technique/apply_refactoring.ipynb` |

---

## Notes on Results.xlsx

`Results.xlsx` records the raw vulnerability counts — identified and fixed — for each project/CWE combination across all five evaluated models. It covers the **2,362 vulnerable instances** drawn from the 20 benchmark projects.

The paper's benchmark total of **10,006 samples** is larger because it includes both the 6,184 vulnerable instances and the 3,822 benign (non-vulnerable) samples used for the overall precision/recall/F1 evaluation in Tables 9–11. The benign samples are not tracked in the spreadsheet since they have no "fix" outcome — they are used solely to measure false-positive rates.

---

## Citation

If you use VulnFixAI in your research, please cite:

```bibtex
@article{ananbeh2025vulnfixai,
  title     = {A Neuro-Symbolic Framework for Automated Vulnerability Detection and Repair in Java Software},
  author    = {Ananbeh, Obieda and Alnozmai, Wala and Kim, Dae-Kyoo},
  journal   = {},
  year      = {2025},
  publisher = {}
}
```
