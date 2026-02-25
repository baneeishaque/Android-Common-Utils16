# Android-Common-Utils16 Development Patterns

> Auto-generated skill from repository analysis

## Overview

This skill covers development patterns for Android-Common-Utils16, a Java-based Android utility library. The codebase focuses on providing common utility functions for Android development with supporting CI/CD infrastructure using Azure Pipelines and PowerShell scripts. The project emphasizes cross-platform build support and modern Java practices.

## Coding Conventions

**File Naming:** Use camelCase for all Java files
```java
// Good
StringUtils.java
DateTimeHelper.java
NetworkManager.java

// Avoid
string_utils.java
datetime-helper.java
```

**Import Style:** Mixed approach with preference for specific imports
```java
// Specific imports preferred
import java.util.List;
import java.util.ArrayList;
import android.content.Context;

// Star imports when importing many classes from same package
import java.util.*;
```

**Package Structure:**
```
common16/src/main/java/ndk/utils_android16/
├── StringUtils.java
├── DateTimeHelper.java
└── NetworkManager.java
```

**Testing:** Test files follow the pattern `*.test.*`
```
src/test/java/SomeClassTest.java
src/test/java/UtilsTest.java
```

## Workflows

### VS Code Settings Update
**Trigger:** When adding new technical terms, tools, or VS Code configuration
**Command:** `/update-vscode-config`

1. Open `.vscode/settings.json`
2. Add new technical terms to the `cSpell.words` array:
   ```json
   {
     "cSpell.words": [
       "gradle",
       "androidx",
       "kotlin"
     ]
   }
   ```
3. Update extension recommendations in `.vscode/extensions.json`
4. Configure editor settings for Java development
5. Commit changes with prefix like "docs: update vscode settings"

### CI Pipeline Refinement
**Trigger:** When optimizing CI/CD performance, adding cross-platform support, or fixing pipeline issues
**Command:** `/refine-pipeline`

1. Identify the target pipeline file:
   - `azure-pipelines-android.yml` for Android builds
   - `azure-pipelines-docker.yml` for Docker builds
2. Modify pipeline configuration:
   ```yaml
   trigger:
     branches:
       include:
       - main
   
   pool:
     vmImage: 'ubuntu-latest'
   ```
3. Update CI scripts in `ci/` directory if needed
4. Adjust caching strategies for dependencies
5. Test pipeline changes on feature branch
6. Commit with descriptive message about the optimization

### CI Script Enhancement
**Trigger:** When improving build reliability, adding platform support, or fixing CI script issues
**Command:** `/enhance-ci-scripts`

1. Modify `ci/common.ps1` for shared helper functions:
   ```powershell
   function Test-CrossPlatformPath {
       param([string]$Path)
       # Add cross-platform path validation
   }
   ```
2. Update `ci/setup-mise-java.ps1` for Java environment setup
3. Enhance error handling in `ci/gradle-build.ps1`:
   ```powershell
   try {
       ./gradlew build
   } catch {
       Write-Error "Build failed: $($_.Exception.Message)"
       exit 1
   }
   ```
4. Test scripts on different platforms (Windows, Linux, macOS)
5. Update documentation in `ci/README.md`

### Documentation Update
**Trigger:** When improving project onboarding, documenting CI processes, or adding project information
**Command:** `/update-docs`

1. Update main `README.md` with:
   - Project description
   - Setup instructions
   - Usage examples
2. Enhance `ci/README.md` with:
   - Detailed CI setup instructions
   - Script descriptions
   - Troubleshooting guide
3. Add code examples for utility usage:
   ```java
   // Example usage
   String result = StringUtils.capitalize("hello world");
   // Returns: "Hello World"
   ```
4. Update any inline documentation in Java files
5. Commit with "docs:" prefix

### Utility Refactoring
**Trigger:** When modernizing code, improving reusability, or consolidating utility functions
**Command:** `/refactor-utils`

1. Identify utility classes in `common16/src/main/java/ndk/utils_android16/`
2. Rename classes to follow camelCase conventions
3. Update code to use modern Java features:
   ```java
   // Before
   List<String> filtered = new ArrayList<>();
   for (String item : items) {
       if (item.length() > 3) {
           filtered.add(item);
       }
   }
   
   // After
   List<String> filtered = items.stream()
       .filter(item -> item.length() > 3)
       .collect(Collectors.toList());
   ```
4. Consolidate similar functionality across utility classes
5. Update tests to match refactored code
6. Update documentation and usage examples

## Testing Patterns

Tests are organized using the `*.test.*` naming pattern:

```java
public class StringUtilsTest {
    @Test
    public void testCapitalize() {
        assertEquals("Hello World", StringUtils.capitalize("hello world"));
    }
    
    @Test
    public void testNullInput() {
        assertNull(StringUtils.capitalize(null));
    }
}
```

## Commands

| Command | Purpose |
|---------|---------|
| `/update-vscode-config` | Add spell checker words, extensions, or VS Code settings |
| `/refine-pipeline` | Optimize Azure Pipeline configurations for better performance |
| `/enhance-ci-scripts` | Improve PowerShell CI scripts for cross-platform compatibility |
| `/update-docs` | Update README files and project documentation |
| `/refactor-utils` | Modernize Java utility classes with current best practices |