# Compiler settings
JAVAC=javac
JAVA=java

# Java compiler flags
JFLAGS=

# Directory paths
SRCDIR=src
BINDIR=bin

# List of Java source files
SOURCES=main.java chip8.java instructions.java screen.java

# Derived objects (class files)
CLASSES=$(SOURCES:%.java=$(BINDIR)/%.class)

# Main class name
MAIN=main

# Target executable
EXECUTABLE=$(BINDIR)/$(MAIN).class

# Default target
all: $(EXECUTABLE)

# Rule to build class files
$(BINDIR)/%.class: $(SRCDIR)/%.java
	$(JAVAC) $(JFLAGS) -d $(BINDIR) $<

# Rule to run the program
run: $(EXECUTABLE)
	$(JAVA) -cp $(BINDIR) $(MAIN)

# Clean target
clean:
	rm -rf $(BINDIR)/*.class

# Phony targets
.PHONY: all run clean
