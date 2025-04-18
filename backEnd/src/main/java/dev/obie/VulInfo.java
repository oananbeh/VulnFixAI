package dev.obie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<String> cweIdentifier;
    private String vulnerabilitySeverity;
    private String vulnerableCodeSnippet;
    private String vulnerabilityDescription;
    private String fixedCodeSnippet;

    @Override
    public String toString() {
        return "VulInfo{" +
                 "cweIdentifier='" + cweIdentifier + '\'' +
                ", vulnerabilitySeverity='" + vulnerabilitySeverity + '\'' +
                ", vulnerableCodeSnippet='" + vulnerableCodeSnippet + '\'' +
                ", vulnerabilityDescription='" + vulnerabilityDescription + '\'' +
                ", fixedCodeSnippet='" + fixedCodeSnippet +
                '}';
    }
}

