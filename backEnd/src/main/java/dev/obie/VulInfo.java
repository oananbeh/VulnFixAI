package dev.obie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @Author: Obieda Ananbeh
 * @Date 10/27/24
 * This class represents a vulnerability information model.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VulInfo {
    private String cweIdentifier;
    private String vulnerabilityName;
    private String vulnerabilitySeverity;
    private String vulnerableCodeSnippet;
    private VulnerabilityLineNumber vulnerabilityLineNumber;
    private String vulnerableClassName;
    private String vulnerableMethodName;
    private String vulnerabilityDescription;
    private String fixedCodeSnippet;
    private FixLocationLineNumber fixAreaLocation;

    @Override
    public String toString() {
        return "VulInfo{" +
                "cweIdentifier='" + cweIdentifier + '\'' +
                ", vulnerabilityName='" + vulnerabilityName + '\'' +
                ", vulnerabilitySeverity='" + vulnerabilitySeverity + '\'' +
                ", vulnerableCodeSnippet='" + vulnerableCodeSnippet + '\'' +
                ", vulnerabilityLineNumber=" + vulnerabilityLineNumber +
                ", vulnerableClassName='" + vulnerableClassName + '\'' +
                ", vulnerableMethodName='" + vulnerableMethodName + '\'' +
                ", vulnerabilityDescription='" + vulnerabilityDescription + '\'' +
                ", fixedCodeSnippet='" + fixedCodeSnippet + '\'' +
                ", fixAreaLocation=" + fixAreaLocation +
                '}';
    }
}

