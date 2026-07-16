<div align="center">

# VulnFixAI

[![Java](https://img.shields.io/badge/Java-17-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.x-61DAFB.svg?logo=react)](https://reactjs.org/)
[![Python](https://img.shields.io/badge/Python-3.8+-blue.svg?logo=python)](https://python.org)
[![Llama](https://img.shields.io/badge/Llama_3.2-3B-orange.svg)](https://ollama.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

### Replication Package for "VulnFixAI: A Neuro-Symbolic Framework for Vulnerability Detection and Repair in Java"

</div>

---

## Overview

VulnFixAI is a neuro-symbolic framework that combines a fine-tuned **Neural Oracle** (Llama 3.2 3B) with a deterministic **Symbolic Enforcer** to detect and repair software vulnerabilities in Java code. The framework decouples vulnerability *localization* (performed by the Neural Oracle) from *remediation* (performed by the Symbolic Enforcer), targeting three vulnerability categories — Lack of Whitelist Validation (LOWV), Lack of Input Sanitization (LOIS), and Inadequate TrustChain Verification (ITV) — using three deterministic, AST-level refactoring algorithms: WVR, OSR, and TCVR. All reported conclusions are bounded to these three template-supported categories under a snippet-level evaluation protocol, not automated vulnerability repair in general.

**Key results (10,006-sample Java benchmark, 20 real-world projects, 16 baselines):**

- **89% overall effectiveness** — 5,504 of 6,184 vulnerable instances identified and repaired (98.2% identification rate × 90.6% fix rate among identified)
- **+30.6 pp EM** over the leading agentic system, CodeRover-S (89.0% vs. 58.4%), and **+25.8 pp EM** over the strongest reasoning-based repair model, Vul-R2 (63.2%)
- **+15 to +22 pp** overall effectiveness over frontier LLMs — ChatGPT-5.2 (+15), Claude 4.5 Sonnet (+19), Gemini 3.0 Flash (+22)
- **+30.0 pp EM (51% relative)** gain over the non-fine-tuned Llama 3.2 (3B) base model
- **86.7%** of exploits blocked (26 of 30) in an independent, executable Vul4J proof-of-vulnerability validation, decoupled from the template-derived reference patches

---

## Repository Structure

```
VulnFixAI/
├── VulnFixAI.ipynb                  # Main training notebook (fine-tuning Llama 3.2 with LoRA/Unsloth)
├── analysis.ipynb                   # Benign sample analysis and dataset construction
├── dl_sota_comparison.ipynb         # Comparison vs. specialized SOTA repair systems (CodeRover-S, Vul-R2, SAN2PATCH, VulMaster, VulRepair, VRepair)
├── sllm_comparison.ipynb            # Comparison vs. general-purpose SLLMs (DeepSeek-Coder, StarCoder-2, etc.)
├── sast_comparison.ipynb            # Comparison vs. SAST tools (SonarQube, Semgrep)
├── APPENDIX.md                      # Supplementary appendix (benchmark projects, failure taxonomy, per-CWE breakdowns)
├── Results.xlsx                     # Per-project raw results (identification + fix counts per model)
│
├── Trining DataSet/                 # Training data
│   ├── VulnFixAI_dataset.csv.zip    # Full training dataset (20,000 Java snippets: 10,000 vulnerable — 2,000 ITV / 5,000 LOIS / 3,000 LOWV — + 10,000 benign)
│   ├── VulnFixAI_dataset.json.zip   # JSON format
│   ├── dataSet_withFixes.csv.zip    # Dataset with applied fixes
│   └── dataset.csv.zip              # Raw dataset
│
├── Evaluation-Benchmark/            # Evaluation data
│   ├── SVD-Benchmark.csv            # 10,006-sample Java benchmark (6,184 vulnerable + 3,822 benign, 20 open-source projects)
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

**RQ1** — To what extent does a neuro-symbolic architecture, which decouples vulnerability localization from remediation, mitigate the "hallucination" and inaccuracy problems inherent in purely generative LLM approaches?

**RQ2** — Can a constrained neuro-symbolic framework outperform state-of-the-art agentic and static analysis tools in identifying and fixing vulnerabilities in real-world Java projects?

**RQ3** — How does the imposition of deterministic refactoring templates affect the system's ability to generalize across diverse vulnerability categories (LOWV, LOIS, ITV)?

---

## Baseline Comparisons

VulnFixAI was evaluated against **16 baselines** across four categories, all under a shared snippet-level protocol (identical Alpaca-formatted inputs, Top-1 prediction, temperature 0). See [APPENDIX.md](APPENDIX.md) for the full per-CWE and per-project breakdowns.

### Specialized SOTA Repair Systems — `dl_sota_comparison.ipynb`

| Model | Repository / Ref | EM (%) | BLEU-4 (%) | CodeBLEU (%) |
|---|---|---|---|---|
| **VulnFixAI** | *(this repo)* | **89.0** | **91.2** | **93.5** |
| Vul-R2 | chain-of-thought reasoning repair model | 63.2 | 68.7 | 72.9 |
| SAN2PATCH | tree-of-thought, sanitizer-log-driven repair | 60.4 | 66.3 | 70.1 |
| CodeRover-S | [nus-apr/auto-code-rover](https://github.com/nus-apr/auto-code-rover) | 58.4 | 64.1 | 68.3 |
| VulMaster | [soarsmu/VulMaster_](https://github.com/soarsmu/VulMaster_) | 51.2 | 60.5 | 65.8 |
| VulRepair | [awsm-research/VulRepair](https://github.com/awsm-research/VulRepair) | 44.0 | 53.7 | 59.1 |
| VRepair | [ASSERT-KTH/VRepair](https://github.com/ASSERT-KTH/VRepair) | 21.9 | 29.3 | 40.9 |

**Reference-free validation of Top-1 patches** (compile rate + manual security review on a 150-instance stratified sample, independent of the template-derived reference patches):

| Model | Compile Rate (%) | Manual Correct (n=150) |
|---|---|---|
| **VulnFixAI** | **97.2** | **89.3% (134/150)** |
| CodeRover-S | 89.6 | 73.3% (110/150) |
| Vul-R2 | 88.1 | 71.3% (107/150) |
| SAN2PATCH | 85.4 | 68.7% (103/150) |
| VulRepair | 76.3 | 58.0% (87/150) |

### General-Purpose Frontier LLMs

Evaluated on the full detection + repair + end-to-end pipeline (Precision / Recall / Accuracy / F1), using standard classification metrics over the 10,006-snippet benchmark:

**Identification**

| Model | Precision (%) | Recall (%) | Accuracy (%) | F1 (%) |
|---|---|---|---|---|
| **VulnFixAI** | **99.1** | **98.2** | **98.3** | **98.6** |
| ChatGPT-5.2 | 94 | 86 | 88.0 | 90 |
| Claude 4.5 Sonnet | 93 | 83 | 85.6 | 88 |
| Gemini 3.0 Flash | 90 | 81 | 82.7 | 85 |
| Llama 3.2 (3B, base) | 84 | 70 | 73.2 | 77 |

**Fix**

| Model | Precision (%) | Recall (%) | Accuracy (%) | F1 (%) |
|---|---|---|---|---|
| **VulnFixAI** | **98** | **81** | **87.2** | **89** |
| ChatGPT-5.2 | 93 | 70 | 78.2 | 80 |
| Claude 4.5 Sonnet | 91 | 67 | 75.5 | 78 |
| Gemini 3.0 Flash | 87 | 64 | 71.8 | 74 |
| Llama 3.2 (3B, base) | 81 | 54 | 63.7 | 65 |

**End-to-End (Detection + Repair)**

| Model | Precision (%) | Recall (%) | Accuracy (%) | F1 (%) |
|---|---|---|---|---|
| **VulnFixAI** | **81** | **81** | **76.5** | **81** |
| ChatGPT-5.2 | 77 | 70 | 68.5 | 74 |
| Claude 4.5 Sonnet | 75 | 67 | 65.8 | 71 |
| Gemini 3.0 Flash | 71 | 64 | 61.6 | 67 |
| Llama 3.2 (3B, base) | 65 | 54 | 53.6 | 59 |

### SLLM Baselines — `sllm_comparison.ipynb`

| Model | HuggingFace / Repository | EM (%) | CodeBLEU (%) | Fix Rate (%) |
|---|---|---|---|---|
| **VulnFixAI (3B)** | *(this repo)* | **89.0** | **93.5** | **90.6** |
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

### Independent Validation — Executable Proof-of-Vulnerability (Vul4J subset, N=30)

To decouple validation from the template-derived reference patches, VulnFixAI's Top-1 patches were re-inserted into their original repositories, compiled, run against regression tests, and checked against Vul4J's exploit-based Proof-of-Vulnerability (PoV) tests:

| Validation Stage | Count | Percentage |
|---|---|---|
| Total evaluated instances | 30 | 100.0% |
| Successful compilation | 29 | 96.7% |
| Regression tests passed | 27 | 90.0% |
| PoV exploit blocked (fully repaired) | 26 | 86.7% |

---

## How to Run

### Prerequisites

- Python 3.8+
- Java 17
- Node.js 18+
- [Ollama](https://ollama.com)
- GPU with at least 8 GB VRAM (for fine-tuning; inference runs on CPU)
- API keys for OpenAI (ChatGPT-5.2), Anthropic (Claude 4.5 Sonnet), and Google (Gemini 3.0 Flash) — only required to reproduce the frontier-LLM baseline comparison

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
| Specialized SOTA comparison (CodeRover-S, Vul-R2, SAN2PATCH, VulMaster, VulRepair, VRepair) | `dl_sota_comparison.ipynb` |
| SLLM baseline comparison | `sllm_comparison.ipynb` |
| SAST tool comparison | `sast_comparison.ipynb` |
| Benign sample analysis | `analysis.ipynb` |
| Refactoring algorithm | `Apply refactoring technique/apply_refactoring.ipynb` |
| Supplementary tables (benchmark projects, per-CWE distribution, failure taxonomy) | `APPENDIX.md` |

---

## Notes on Results.xlsx

`Results.xlsx` records the raw vulnerability counts — identified and fixed — for each project/CWE combination. It covers the **2,362 vulnerable instances** from a related prior comparative study (ChatGPT-4, Claude 3.5 Sonnet, Gemini 2.0 Flash, and Llama 3.2 across the same 20 Java projects), which the current paper references as prior work rather than reruns.

The current paper's benchmark of **10,006 samples** is a separate, larger evaluation: it comprises 6,184 vulnerable instances and 3,822 benign (non-vulnerable) samples, evaluated against the newer model generations (ChatGPT-5.2, Claude 4.5 Sonnet, Gemini 3.0 Flash) and reported in Tables X–XII of the paper. The benign samples are used solely to measure false-positive rates and are not tracked in `Results.xlsx`.

---

## Citation

If you use VulnFixAI in your research, please cite:

```bibtex
@article{ananbeh2026vulnfixai,
  title     = {VulnFixAI: A Neuro-Symbolic Framework for Vulnerability Detection and Repair in Java},
  author    = {Ananbeh, Obieda and Alnozami, Wala and Kim, Dae-Kyoo},
  journal   = {},
  year      = {2026},
  publisher = {}
}
```

## License

This project is licensed under the **MIT License** 
