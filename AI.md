# AI Tools Usage Record

## Tool Used
**Primary Tool:** Claude (Anthropic)  

## Usage Summary

### Week 1-2: Initial Development
- **Javadocs Generation**: Used Claude to generate comprehensive Javadocs for all classes and methods. Claude excelled at understanding method signatures and generating appropriate documentation, though it occasionally missed project-specific context that required manual adjustments.

- **Code Review Assistance**: Asked Claude to review code structure and suggest improvements for better OOP practices. Particularly helpful for refactoring the command parsing logic.

### Week 3-4: Feature Implementation
- **Error Handling**: Used Claude to identify potential edge cases in command parsing and suggest appropriate exception handling strategies. This saved approximately 2-3 hours of debugging time.

- **Test Case Generation**: Asked Claude to suggest test cases for the Parser and TaskList classes. While the suggestions were good starting points, I had to modify about 40% of them to match my specific implementation details.

### Week 5: GUI Development
- **JavaFX Troubleshooting**: Claude helped debug CSS styling issues and explained JavaFX binding concepts when implementing the dialog boxes. However, it couldn't always predict the exact behavior of my specific JavaFX setup.

## What Worked Well
1. **Documentation**: Helpful in generating Javadocs and inline comments
2. **Boilerplate Code**: Quick generation of getters, setters, and standard methods
3. **Design Patterns**: Helpful explanations of Command pattern implementation
4. **Debugging Assistance**: Good at spotting potential NullPointerExceptions and suggesting defensive programming practices

## What Didn't Work
1. **Context Limitations**: Claude doesn't maintain full project context, requiring me to re-explain project structure for complex queries
2. **Integration Issues**: Suggestions sometimes didn't align with existing code architecture
3. **Over-Engineering**: Occasionally suggested overly complex solutions for simple problems

## Key Observations
- AI tools are most effective for well-defined, isolated tasks (like writing Javadocs for a single method)
- Particularly valuable for learning new APIs (JavaFX in this case) through examples and explanations

## Interesting Finding
Claude was surprisingly effective at suggesting assertion statements for defensive programming, catching edge cases I hadn't considered initially. This improved code robustness significantly.

---
*Last Updated: September 2025*