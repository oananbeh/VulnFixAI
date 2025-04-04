# apply WVR algorithm
import pandas as pd
import re

def extract_input_data(code):
    """Enhanced input data extraction with more comprehensive patterns."""
    input_patterns = [
        # HTTP Parameters and Annotations
        r'@PathParam\("([^"]+)"\)',
        r'@QueryParam\("([^"]+)"\)',
        r'@FormParam\("([^"]+)"\)',
        r'@RequestParam\("([^"]+)"\)',
        r'@ModelAttribute\s+\w+\s+(\w+)',
        r'getParameter\("([^"]+)"\)',
        
        # Message and Resource Handling
        r'getMessage\(([^,)]+)',
        r'ResourceBundle\.getBundle\("([^"]+)"',
        r'format\("([^"]+)"',
        r'String\.format\("([^"]+)"',
        r'MessageFormat\.format\("([^"]+)"',
        
        # Variable Declarations and Assignments
        r'String\s+(\w+)\s*=',
        r'(\w+)\s*=\s*[^=]+getParameter',
        r'(\w+)\s*=\s*[^=]+getMessage',
        r'(\w+)\s*=\s*[^=]+format',
        
        # Method Parameters
        r'public\s+[\w<>[\],\s]+\s+\w+\(([^)]+)\)',
        r'private\s+[\w<>[\],\s]+\s+\w+\(([^)]+)\)',
        r'protected\s+[\w<>[\],\s]+\s+\w+\(([^)]+)\)',
        
        # Additional Format String Patterns
        r'\.format\(([^)]+)\)',
        r'\.printf\(([^)]+)\)',
        r'PrintStream\.printf\(([^)]+)\)',
        r'Formatter\.format\(([^)]+)\)',
        
        # Logger and Error Messages
        r'logger\.(error|warn|info|debug)\(([^)]+)\)',
        r'LOG\.(error|warn|info|debug)\(([^)]+)\)',
        r'throw new \w+Exception\(([^)]+)\)',
        
        # Resource and File Operations
        r'new File\("([^"]+)"\)',
        r'Paths\.get\("([^"]+)"\)',
        r'createTempFile\("([^"]+)"',
        r'FileInputStream\("([^"]+)"\)',
        r'FileOutputStream\("([^"]+)"\)',
        
        # Additional Input Sources
        r'request\.getAttribute\("([^"]+)"\)',
        r'session\.getAttribute\("([^"]+)"\)',
        r'cookie\.getValue\("([^"]+)"\)',
        r'headers\.get\("([^"]+)"\)'
    ]
    
    inputs = set()
    for pattern in input_patterns:
        try:
            matches = re.finditer(pattern, code)
            for match in matches:
                # Extract all capturing groups
                groups = match.groups()
                for group in groups:
                    if group and isinstance(group, str):
                        # Clean and validate the input
                        cleaned = group.strip().strip('"\'')
                        if cleaned and not cleaned.startswith('{'): 
                            inputs.add(cleaned)
        except Exception:
            continue
    
    # Extract method parameters
    try:
        method_params = re.findall(r'(\w+)\s+(\w+)(?=\s*[,)])', code)
        inputs.update(param[1] for param in method_params)
    except Exception:
        pass
    
    return list(inputs)

def determine_whitelist_pattern(input_data):
    """Enhanced whitelist pattern determination with more specific patterns."""
    patterns = {
        # File System Patterns
        'file': r'^[\w\-. /\\]+$',
        'path': r'^[\w\-. /\\]+$',
        'dir': r'^[\w\-. /\\]+$',
        'temp': r'^[\w\-. ]+$',
        
        # Web and URL Patterns
        'url': r'^[a-zA-Z0-9\-._~:/?#\[\]@!$&\'()*+,;=\s]+$',
        'uri': r'^[a-zA-Z0-9\-._~:/?#\[\]@!$&\'()*+,;=\s]+$',
        'http': r'^https?://[\w\-._~:/?#\[\]@!$&\'()*+,;=\s]+$',
        
        # Identity and Authentication
        'realm': r'^[\w\-]+$',
        'user': r'^[\w\-@.]+$',
        'email': r'^[\w\-\.]+@([\w\-]+\.)+[\w\-]{2,4}$',
        'password': r'^[\S]+$',
        'token': r'^[\w\-]+$',
        
        # Format and Content Type
        'format': r'^[\w\-/+.]+$',
        'type': r'^[\w\-/+.]+$',
        'content': r'^[\w\-/+.]+$',
        'mime': r'^[\w\-/+.]+$',
        
        # Message and Locale
        'bundle': r'^[\w._]+$',
        'locale': r'^[a-z]{2}(-[A-Z]{2})?$',
        'message': r'^[\w\-._{}\s]+$',
        'error': r'^[\w\-._{}\s]+$',
        'log': r'^[\w\-._{}\s]+$',
        
        # Database and Query
        'id': r'^\d+$',
        'query': r'^[\w\s\-=><&|()]+$',
        'key': r'^[\w\-]+$',
        
        # Numbers and Dates
        'number': r'^\d+$',
        'date': r'^\d{4}-\d{2}-\d{2}$',
        'time': r'^\d{2}:\d{2}:\d{2}$',
        'timestamp': r'^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(.\d{3})?Z?$',
        
        # Default Pattern
        'default': r'^[\w\-._\s]+$'
    }
    
    input_lower = input_data.lower()
    
    # Context-based pattern matching
    if re.search(r'file|path|directory', input_lower):
        return patterns['file']
    elif re.search(r'url|uri|http', input_lower):
        return patterns['url']
    elif re.search(r'date|time|timestamp', input_lower):
        return patterns['timestamp']
    elif re.search(r'id|key|index', input_lower):
        return patterns['id']
    elif re.search(r'message|error|log', input_lower):
        return patterns['message']
    elif re.search(r'format|type|mime', input_lower):
        return patterns['format']
    
    # Use default pattern if no specific match found
    return patterns['default']

def build_validation(input_data, pattern):
    """Build comprehensive validation with additional security checks."""
    return f"""
    // Input validation for {input_data}
    if ({input_data} != null) {{
        // Length check
        if ({input_data}.length() > 1000) {{
            throw new IllegalArgumentException("{input_data} exceeds maximum length");
        }}
        // Pattern validation
        if (!Pattern.matches("{pattern}", {input_data})) {{
            throw new IllegalArgumentException("Invalid format for {input_data}");
        }}
        // Sanitize input
        {input_data} = {input_data}
            .trim()
            .replaceAll("[\\r\\n]", "")
            .replaceAll("[\\x00-\\x1F\\x7F]", "");
            
        // Additional security check for file paths
        if ({input_data}.contains("..")) {{
            throw new IllegalArgumentException("Path traversal attempt detected");
        }}
    }}
    """

def build_input_handling(validation, original_code):
    """Enhanced input handling with better code structure preservation."""
    try:
        lines = original_code.split('\n')
        modified_lines = []
        validation_added = False
        
        # Common vulnerability patterns
        vuln_patterns = [
            'getMessage', 'ResourceBundle.getBundle', '@PathParam',
            'getParameter', 'format', 'new File', 'Paths.get',
            'createTempFile', 'FileInputStream', 'FileOutputStream',
            'getAttribute', 'getValue', 'headers.get'
        ]
        
        # Method declaration pattern
        method_pattern = re.compile(r'(public|private|protected)\s+[\w<>[\],\s]+\s+\w+\s*\(')
        
        inside_method = False
        for line in lines:
            # Check if we're entering a method
            if method_pattern.search(line):
                inside_method = True
                modified_lines.append(line)
                if not validation_added:
                    modified_lines.append(validation)
                    validation_added = True
                continue
            
            # Add validation before vulnerable lines
            if inside_method and not validation_added:
                if any(pattern in line for pattern in vuln_patterns):
                    modified_lines.append(validation)
                    validation_added = True
            
            modified_lines.append(line)
        
        return '\n'.join(modified_lines)
    except Exception as e:
        print(f"Error in build_input_handling: {str(e)}")
        return original_code

def apply_wvr(code_snippet):
    """Apply enhanced WVR with better error handling and logging."""
    if not isinstance(code_snippet, str) or not code_snippet.strip():
        return code_snippet
    
    try:
        # Extract all potential input points
        inputs = extract_input_data(code_snippet)
        
        if not inputs:
            return code_snippet
        
        # Apply WVR for each input
        modified_code = code_snippet
        for input_data in inputs:
            pattern = determine_whitelist_pattern(input_data)
            validation = build_validation(input_data, pattern)
            modified_code = build_input_handling(validation, modified_code)
        
        return modified_code
    except Exception as e:
        print(f"Error processing code snippet: {str(e)}")
        return code_snippet

def main():
    try:
        # Read the CSV file
        df = pd.read_csv('/LOWV.csv')
        print(f"Total rows in dataset: {len(df)}")
        
        # Apply WVR to each code snippet
        df['code_fix'] = df['Code Snippet'].apply(apply_wvr)
        
        # Calculate statistics
        modified_count = (df['Code Snippet'] != df['code_fix']).sum()
        print(f"Number of snippets modified: {modified_count}")
        print(f"Coverage percentage: {(modified_count/len(df))*100:.2f}%")
        
        # Save the results
        df.to_csv('LOWV_results.csv', index=False)
        print("WVR processing completed. Results saved to 'wvr_results.csv'")
        
    except Exception as e:
        print(f"Error in main processing: {str(e)}")

if __name__ == "__main__":
    main()