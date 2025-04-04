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
      return prevDiff.map(line => {
        if (line.lineNum === lineNumber && line.content.includes(vulnerableCode.trim())) {
          // Split the fix code into lines and get the first line
          const fixLines = fixCode.trim().split('\n');
          
          if (fixLines.length === 1) {
            // Simple single-line replacement
            const newContent = line.content.replace(vulnerableCode.trim(), fixCode.trim());
            return { ...line, content: newContent, type: 'fixed' };
          } else {
            // For multi-line fixes, we'll need to splice in the new lines
            const updatedLines = fixLines.map((fixLine, index) => ({
              content: fixLine,
              lineNum: (parseInt(lineNumber) + index).toString(),
              type: 'fixed'
            }));
            return updatedLines[0];
          }
        }
        return line;
      });
    });

    // Add the line number to fixedLines set
    setFixedLines(prev => new Set([...prev, lineNumber]));
    
    // Remove the fixed vulnerability from the list
    setVulnerabilities(prev => 
      prev.filter(v => v.vulnerable_code !== vulnerability.vulnerable_code)
    );

    // Close the tooltip after applying the fix
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

  const getLineColor = (type, content, lineNum) => {
    // If the line has been fixed, show it in green
    if (type === 'fixed' || fixedLines.has(lineNum)) {
      return 'bg-green-100';
    }

    // Check if this line contains vulnerable code
    const isVulnerable = vulnerabilities.some(v => 
      content.includes(v.vulnerable_code.trim())
    );

    if (isVulnerable) {
      return 'bg-red-100';
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
          const isVulnerable = vulnerabilities.some(v => 
            line.content.includes(v.vulnerable_code.trim())
          );

          const lineVulnerabilities = vulnerabilities.filter(v => 
            line.content.includes(v.vulnerable_code.trim())
          );

          const isFixed = fixedLines.has(line.lineNum);

          return (
            <div key={index} className="relative group">
              <div className={`flex ${getLineColor(line.type, line.content, line.lineNum)} hover:bg-gray-50`}>
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

              {/* Vulnerability Tooltip */}
              {showTooltip === line.lineNum && lineVulnerabilities.length > 0 && (
                <div className="ml-12 mt-1 p-3 bg-white shadow-lg rounded border border-red-200 z-10">
                  {lineVulnerabilities.map((v, i) => (
                    <div key={i} className="text-sm mb-4 last:mb-0">
                      <div className="flex items-start justify-between">
                        <p className="font-semibold text-red-600">{v.cwe_id}</p>
                        <button 
                          onClick={() => setShowTooltip(null)}
                          className="text-gray-400 hover:text-gray-600"
                        >
                          <X size={16} />
                        </button>
                      </div>
                      <p className="mt-1 text-gray-700">{v.description}</p>
                      <div className="mt-3 bg-gray-50 p-3 rounded">
                        <div className="flex items-center justify-between mb-2">
                          <p className="font-semibold text-gray-700">Suggested fix:</p>
                          <button
                            onClick={() => applyFix(line.lineNum, v.vulnerable_code, v.fix_vulnerable_code, v)}
                            className="flex items-center gap-1 px-2 py-1 bg-green-500 text-white rounded hover:bg-green-600 text-xs"
                          >
                            <Check size={12} />
                            Apply Fix
                          </button>
                        </div>
                        <pre className="p-2 bg-white rounded text-xs whitespace-pre-wrap border border-gray-200">
                          {v.fix_vulnerable_code}
                        </pre>
                      </div>
                    </div>
                  ))}
                </div>
              )}

              {/* Comments Section */}
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

              {/* Add Comment Form */}
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