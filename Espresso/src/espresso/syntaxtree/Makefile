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
AdditiveExpNode.class \
AllocationExpNode.class \
AndExpNode.class \
ArrayExpNode.class \
ArrayInitializerNode.class \
AssignmentNode.class \
BlockNode.class \
BooleanLiteral.class \
BreakStatementNode.class \
CastExpNode.class \
CharacterLiteral.class \
ClassBodyDeclarationNode.class \
ClassDeclarationNode.class \
CompilationUnitNode.class \
ConditionalAndExpNode.class \
ConditionalExpNode.class \
ConditionalOrExpNode.class \
ConstructorDeclarationNode.class \
ConstructorInvocationNode.class \
ContinueStatementNode.class \
DoStatementNode.class \
DoubleLiteral.class \
EmptyStatementNode.class \
EqualityExpNode.class \
ExclusiveOrExpNode.class \
ExpressionNode.class \
FieldDeclarationNode.class \
FieldExpNode.class \
FloatLiteral.class \
ForStatementNode.class \
FormalExpNode.class \
FormalParameterNode.class \
IfStatementNode.class \
InclusiveOrExpNode.class \
InstanceOfExpNode.class \
IntegerLiteral.class \
InterfaceDeclarationNode.class \
InterfaceMemberDeclarationNode.class \
LabeledStatementNode.class \
Literal.class \
LocalExpNode.class \
LocalVarDeclarationNode.class \
LongLiteral.class \
MethodDeclarationNode.class \
MethodExpNode.class \
MultiplicativeExpNode.class \
NullLiteral.class \
PackageExpNode.class \
PreDecrementExpNode.class \
PreIncrementExpNode.class \
PostDecrementExpNode.class \
PostIncrementExpNode.class \
RelationalExpNode.class \
ReturnStatementNode.class \
ShiftExpNode.class \
StatementExpListNode.class \
StatementExpNode.class \
StatementNode.class \
StaticInitializerNode.class \
StringLiteral.class \
SuperExpNode.class \
SwitchLabelNode.class \
SwitchStatementNode.class \
SymbolExpNode.class \
SynchronizedStatementNode.class \
SyntaxTreeNode.class \
ThisExpNode.class \
ThrowStatementNode.class \
TryStatementNode.class \
TypeDeclarationNode.class \
TypeExpNode.class \
UnaryMinusExpNode.class \
UnaryNegExpNode.class \
UnaryNotExpNode.class \
UnaryPlusExpNode.class \
VariableExpNode.class \
VariableDeclaratorNode.class \
WhileStatementNode.class 

all: $(FILES)

####################################################################

