import React, { useState } from 'react';
import { Upload, FileText, Loader } from 'lucide-react';
import CodeReviewComponent from './CodeReviewComponent';
import JSZip from 'jszip';

const App = () => {
  const [files, setFiles] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [isUploaded, setIsUploaded] = useState(false);
  const [vulnerabilities, setVulnerabilities] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const handleFileUpload = async (event) => {
    const uploadedFile = event.target.files[0];
    if (uploadedFile && uploadedFile.name.endsWith('.zip')) {
      try {
        const zip = new JSZip();
        const zipContents = await zip.loadAsync(uploadedFile);
        const javaFiles = [];

        // Process all files in the ZIP
        for (const [filename, file] of Object.entries(zipContents.files)) {
          if (filename.endsWith('.java') && !file.dir) {
            const content = await file.async('string');
            javaFiles.push({
              name: filename.split('/').pop(), // Get just the filename
              content: content
            });
          }
        }

        setFiles(javaFiles);
        setIsUploaded(true);
      } catch (error) {
        console.error('Error processing ZIP:', error);
        alert('Error processing ZIP file');
      }
    } else {
      alert('Please upload a ZIP file');
    }
  };

  const analyzeCode = async (content) => {
    setIsLoading(true);
    try {
      const response = await fetch('http://localhost:8080/v2/vul', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ code: content })
      });
      
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      
      const vulnerabilityData = await response.json();
      setVulnerabilities(vulnerabilityData);
    } catch (error) {
      console.error('Error:', error);
      alert('Error analyzing code');
    } finally {
      setIsLoading(false);
    }
  };

  const handleFileSelect = (file) => {
    setSelectedFile(file);
    setVulnerabilities([]); // Clear previous vulnerabilities
    analyzeCode(file.content);
  };

  const getCodeDiff = (content) => {
    return content.split('\n').map((line, index) => {
      const lineNumber = index + 1;
      const vulnerability = vulnerabilities.find(v => parseInt(v.line_number) === lineNumber);
      
      return {
        type: vulnerability ? 'vulnerable' : 'unchanged',
        content: line,
        lineNum: lineNumber,
        vulnerability: vulnerability
      };
    });
  };

  if (!isUploaded) {
    return (
      <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center p-4">
        <div className="max-w-xl w-full bg-white rounded-lg shadow-lg p-8">
          <h1 className="text-2xl font-bold text-center mb-8">
            Upload Your Java Project
          </h1>
          
          <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center">
            <Upload className="mx-auto h-12 w-12 text-gray-400 mb-4" />
            <label className="block">
              <span className="sr-only">Choose ZIP file</span>
              <input
                type="file"
                className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                accept=".zip"
                onChange={handleFileUpload}
              />
            </label>
            <p className="mt-2 text-sm text-gray-500">
              Please upload your Java project as a ZIP file
            </p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-lg">
        <div className="p-4 border-b">
          <h2 className="text-lg font-semibold">Java Files</h2>
          <p className="text-sm text-gray-500 mt-1">
            {files.length} file{files.length !== 1 ? 's' : ''} found
          </p>
        </div>
        <div className="overflow-y-auto">
          {files.map((file, index) => (
            <button
              key={index}
              onClick={() => handleFileSelect(file)}
              className={`w-full px-4 py-2 text-left flex items-center space-x-2 hover:bg-gray-50 ${
                selectedFile?.name === file.name ? 'bg-blue-50' : ''
              }`}
            >
              <FileText size={16} />
              <span>{file.name}</span>
            </button>
          ))}
        </div>
      </div>

      {/* Main content */}
      <div className="flex-1 overflow-auto p-4">
        {isLoading ? (
          <div className="h-full flex items-center justify-center">
            <Loader className="w-8 h-8 animate-spin text-blue-500" />
          </div>
        ) : selectedFile ? (
          <CodeReviewComponent 
            codeDiff={getCodeDiff(selectedFile.content)} 
            vulnerabilities={vulnerabilities}
          />
        ) : (
          <div className="h-full flex items-center justify-center text-gray-500">
            Select a file to review
          </div>
        )}
      </div>
    </div>
  );
};

export default App;