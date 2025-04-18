import React, { useState } from 'react';
import { ThumbsUp, X, Plus, AlertTriangle, Check } from 'lucide-react';

const CodeReviewComponent = ({ codeDiff: initialCodeDiff, vulnerabilities: initialVulnerabilities }) => {
  const [comments, setComments] = useState({});
  const [showAddComment, setShowAddComment] = useState(null);
  const [newComment, setNewComment] = useState('');
  const [showTooltip, setShowTooltip] = useState(null);
  const [codeDiff, setCodeDiff] = useState(initialCodeDiff);
  const [vulnerabilities, setVulnerabilities] = useState(initialVulnerabilities);
  const [fixedLines, setFixedLines] = useState(new Set());

  const handleAddComment = (lineNum) => {
    if (newComment.trim()) {
      setComments(prev => ({
        ...prev,
        [lineNum]: [...(prev[lineNum] || []), {
          id: Date.now(),
          text: newComment,
          author: 'Current User',
          timestamp: new Date().toISOString(),
          resolved: false
        }]
      }));
      setNewComment('');
      setShowAddComment(null);
    }
  };

  const applyFix = (lineNumber, vulnerableCode, fixCode, vulnerability) => {
    setCodeDiff(prevDiff => {
      const lineIndex = prevDiff.findIndex(line => line.lineNum === lineNumber);
      if (lineIndex === -1) return prevDiff;

      const originalLine = prevDiff[lineIndex];
      const trimmedVulnerableCode = typeof vulnerableCode === 'string' ? vulnerableCode.trim() : '';

      if (originalLine.content.includes(trimmedVulnerableCode)) {
        const fixLines = typeof fixCode === 'string' ? fixCode.trim().split('\n') : [];

        if (fixLines.length === 1) {
          const newContent = originalLine.content.replace(trimmedVulnerableCode, fixLines[0]);
          const updatedDiff = [...prevDiff];
          updatedDiff[lineIndex] = { ...originalLine, content: newContent, type: 'fixed' };
          return updatedDiff;
        } else if (fixLines.length > 1) {
          const newContent = originalLine.content.replace(trimmedVulnerableCode, fixLines[0]);
          const updatedDiff = [...prevDiff];
          updatedDiff[lineIndex] = { ...originalLine, content: newContent, type: 'fixed' };
          return updatedDiff;
        }
      }
      return prevDiff;
    });

    setFixedLines(prev => new Set([...prev, lineNumber]));

    setVulnerabilities(prev => 
      prev.filter(v => v.vulnerableCodeSnippet !== vulnerability.vulnerableCodeSnippet)
    );

    setShowTooltip(null);
  };

  const toggleResolveComment = (lineNum, commentId) => {
    setComments(prev => ({
      ...prev,
      [lineNum]: prev[lineNum].map(comment => 
        comment.id === commentId 
          ? { ...comment, resolved: !comment.resolved }
          : comment
      )
    }));
  };

  const deleteComment = (lineNum, commentId) => {
    setComments(prev => ({
      ...prev,
      [lineNum]: prev[lineNum].filter(comment => comment.id !== commentId)
    }));
  };

  const getLineColor = (type, lineNum) => {
    if (type === 'fixed' || fixedLines.has(lineNum)) {
      return 'bg-green-100';
    }

    switch (type) {
      case 'vulnerable':
        return 'bg-red-100';
      case 'added':
        return 'bg-green-50';
      case 'removed':
        return 'bg-red-50';
      default:
        return 'bg-white';
    }
  };

  return (
    <div className="w-full bg-white rounded-lg shadow">
      <div className="mb-4 p-2 bg-gray-100 rounded-t">
        <h2 className="text-lg font-semibold">Code Review</h2>
        {vulnerabilities.length > 0 && (
          <p className="text-sm text-red-600 mt-1">
            {vulnerabilities.length} vulnerability{vulnerabilities.length !== 1 ? 'ies' : ''} found
          </p>
        )}
      </div>
      
      <div className="font-mono text-sm">
        {codeDiff.map((line, index) => {
          const isVulnerable = line.type === 'vulnerable'; 
          const isFixed = line.type === 'fixed' || fixedLines.has(line.lineNum);
          const lineVulnerability = line.vulnerability; 

          return (
            <div key={index} className="relative group">
              <div className={`flex ${getLineColor(line.type, line.lineNum)} hover:bg-gray-50`}>
                <div className="w-12 text-gray-500 text-right pr-4 select-none">
                  {line.lineNum}
                </div>
                <div className="flex-1 relative">
                  <pre className="p-1">
                    <span className={isVulnerable ? 'text-red-700 font-semibold' : 
                                  isFixed ? 'text-green-700 font-semibold' : ''}>
                      {line.content}
                    </span>
                  </pre>
                  
                  {isVulnerable && (
                    <div className="absolute right-8 top-1/2 -translate-y-1/2">
                      <button 
                        className="focus:outline-none"
                        onClick={() => setShowTooltip(showTooltip === line.lineNum ? null : line.lineNum)}
                      >
                        <AlertTriangle className="h-5 w-5 text-red-500" />
                      </button>
                    </div>
                  )}
                  
                  <button
                    onClick={() => setShowAddComment(line.lineNum)}
                    className="opacity-0 group-hover:opacity-100 absolute right-2 top-1/2 -translate-y-1/2 p-1 hover:bg-gray-200 rounded"
                  >
                    <Plus size={16} />
                  </button>
                </div>
              </div>

              {showTooltip === line.lineNum && lineVulnerability && (
                <div className="ml-12 mt-1 p-3 bg-white shadow-lg rounded border border-red-200 z-10">
                  <div className="text-sm mb-4 last:mb-0">
                    <div className="flex items-start justify-between">
                      <p className="font-semibold text-red-600">{lineVulnerability.cweIdentifier || lineVulnerability.cwe_id}</p> 
                      <button 
                        onClick={() => setShowTooltip(null)}
                        className="text-gray-400 hover:text-gray-600"
                      >
                        <X size={16} />
                      </button>
                    </div>
                    <p className="mt-1 text-gray-700">{lineVulnerability.vulnerabilityDescription || lineVulnerability.description}</p>
                    <div className="mt-3 bg-gray-50 p-3 rounded">
                      <div className="flex items-center justify-between mb-2">
                        <p className="font-semibold text-gray-700">Suggested fix:</p>
                        <button
                          onClick={() => applyFix(line.lineNum, lineVulnerability.vulnerableCodeSnippet, lineVulnerability.fixedCodeSnippet, lineVulnerability)}
                          className="flex items-center gap-1 px-2 py-1 bg-green-500 text-white rounded hover:bg-green-600 text-xs"
                        >
                          <Check size={12} />
                          Apply Fix
                        </button>
                      </div>
                      <pre className="p-2 bg-white rounded text-xs whitespace-pre-wrap border border-gray-200">
                        {lineVulnerability.fixedCodeSnippet || lineVulnerability.fix_vulnerable_code}
                      </pre>
                    </div>
                  </div>
                </div>
              )}

              {comments[line.lineNum]?.map(comment => (
                <div key={comment.id} className="ml-12 p-2 border-l-2 border-blue-200 bg-blue-50 mt-1">
                  <div className="flex justify-between items-start">
                    <div>
                      <span className="font-semibold">{comment.author}</span>
                      <span className="text-gray-500 text-xs ml-2">
                        {new Date(comment.timestamp).toLocaleString()}
                      </span>
                    </div>
                    <div className="flex gap-2">
                      <button
                        onClick={() => toggleResolveComment(line.lineNum, comment.id)}
                        className={`p-1 rounded ${comment.resolved ? 'text-green-600' : 'text-gray-400'}`}
                      >
                        <ThumbsUp size={16} />
                      </button>
                      <button
                        onClick={() => deleteComment(line.lineNum, comment.id)}
                        className="p-1 rounded text-gray-400 hover:text-red-600"
                      >
                        <X size={16} />
                      </button>
                    </div>
                  </div>
                  <p className="mt-1 text-sm">{comment.text}</p>
                </div>
              ))}

              {showAddComment === line.lineNum && (
                <div className="ml-12 p-2 border-l-2 border-blue-200 bg-blue-50 mt-1">
                  <textarea
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    className="w-full p-2 border rounded resize-y"
                    placeholder="Add a comment..."
                    rows={3}
                  />
                  <div className="mt-2 flex gap-2">
                    <button
                      onClick={() => handleAddComment(line.lineNum)}
                      className="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
                    >
                      Add Comment
                    </button>
                    <button
                      onClick={() => setShowAddComment(null)}
                      className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default CodeReviewComponent;