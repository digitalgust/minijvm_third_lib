####################################################################
#                                                                  #
# Makefile for Espresso                                            #
#                                                                  #
####################################################################

####################################################################
# Useful macros                                                    #
####################################################################

JAVAC = javac
JAVAC_FLAGS =
JAVACC = javacc
JAVACC_FLAGS = -static=FALSE

####################################################################
#  Explicit rules                                                  #
####################################################################

.SUFFIXES: .java .class .jj

.java.class:
	$(JAVAC) $(JAVAC_FLAGS) $<

.jj.java:
	$(JAVACC) $(JAVACC_FLAGS) $<

####################################################################
#  Modules                                                         #
####################################################################

FILES = \
Attribute.class \
ClassParser.class \
Code.class \
CodeException.class \
Constant.class \
ConstantCP.class \
ConstantClass.class \
ConstantDouble.class \
ConstantFieldref.class \
ConstantFloat.class \
ConstantInteger.class \
ConstantInterfaceMethodref.class \
ConstantLong.class \
ConstantMethodref.class \
ConstantNameAndType.class \
ConstantPool.class \
ConstantString.class \
ConstantUnicode.class \
ConstantUtf8.class \
ConstantValue.class \
Deprecated.class \
ExceptionTable.class \
Field.class \
InnerClass.class \
InnerClasses.class \
JavaClass.class \
LineNumber.class \
LineNumberTable.class \
LocalVariable.class \
LocalVariableTable.class \
Method.class \
SourceFile.class \
Synthetic.class \
Unknown.class \
Utility.class \
Visitor.class 

all: $(FILES)

####################################################################

