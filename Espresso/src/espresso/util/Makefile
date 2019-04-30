####################################################################
#                                                                  #
# Makefile for Espresso                                            #
#                                                                  #
####################################################################

####################################################################
# Useful macros                                                    #
####################################################################

JAVAC = javac
JAVAC_FLAGS = -g
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
AmbiguousName.class \
ArrayType.class \
BooleanType.class \
ByteType.class \
CharType.class \
ClassType.class \
DoubleType.class \
ErrorMsg.class \
FloatType.class \
FloatingPointType.class \
IntType.class \
IntegerStack.class \
IntegerStream.class \
IntegralType.class \
InternalError.class \
LongType.class \
MethodDesc.class \
MethodType.class \
NotConstant.class \
NullType.class \
Operators.class \
PrimitiveType.class \
ReferenceType.class \
ShortType.class \
Symbol.class \
SymbolTable.class \
Type.class \
TypeCheckError.class \
VoidType.class

all: $(FILES)

####################################################################

