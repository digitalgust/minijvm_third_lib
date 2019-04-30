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
AALOAD.class \
AASTORE.class \
ACONST_NULL.class \
ALOAD.class \
ANEWARRAY.class \
ARETURN.class \
ARRAYLENGTH.class \
ASTORE.class \
ATHROW.class \
BALOAD.class \
BASTORE.class \
BIPUSH.class \
BranchHandle.class \
BranchInstruction.class \
CALOAD.class \
CASTORE.class \
CHECKCAST.class \
CPInstruction.class \
ClassGen.class \
ClassGenException.class \
ClassGenType.class \
CodeConstraint.class \
CompoundInstruction.class \
ConstantPoolGen.class \
D2F.class \
D2I.class \
D2L.class \
DADD.class \
DALOAD.class \
DASTORE.class \
DCMPG.class \
DCMPL.class \
DCONST.class \
DDIV.class \
DLOAD.class \
DMUL.class \
DNEG.class \
DREM.class \
DRETURN.class \
DSTORE.class \
DSUB.class \
DUP.class \
DUP2.class \
DUP2_X1.class \
DUP2_X2.class \
DUP_X1.class \
DUP_X2.class \
F2D.class \
F2I.class \
F2L.class \
FADD.class \
FALOAD.class \
FASTORE.class \
FCMPG.class \
FCMPL.class \
FCONST.class \
FDIV.class \
FLOAD.class \
FMUL.class \
FNEG.class \
FREM.class \
FRETURN.class \
FSTORE.class \
FSUB.class \
# FindPattern.class \
GETFIELD.class \
GETSTATIC.class \
GOTO.class \
GOTO_W.class \
I2B.class \
I2C.class \
I2D.class \
I2F.class \
I2L.class \
I2S.class \
IADD.class \
IALOAD.class \
IAND.class \
IASTORE.class \
ICONST.class \
IDIV.class \
IFEQ.class \
IFGE.class \
IFGT.class \
IFLE.class \
IFLT.class \
IFNE.class \
IFNONNULL.class \
IFNULL.class \
IF_ACMPEQ.class \
IF_ACMPNE.class \
IF_ICMPEQ.class \
IF_ICMPGE.class \
IF_ICMPGT.class \
IF_ICMPLE.class \
IF_ICMPLT.class \
IF_ICMPNE.class \
IINC.class \
ILOAD.class \
IMPDEP1.class \
IMPDEP2.class \
IMUL.class \
INEG.class \
INSTANCEOF.class \
INVOKEINTERFACE.class \
INVOKESPECIAL.class \
INVOKESTATIC.class \
INVOKEVIRTUAL.class \
IOR.class \
IREM.class \
IRETURN.class \
ISHL.class \
ISHR.class \
ISTORE.class \
ISUB.class \
IUSHR.class \
IXOR.class \
Instruction.class \
InstructionHandle.class \
InstructionList.class \
JSR.class \
JSR_W.class \
L2D.class \
L2F.class \
L2I.class \
LADD.class \
LALOAD.class \
LAND.class \
LASTORE.class \
LCMP.class \
LCONST.class \
LDC.class \
LDC2_W.class \
LDC_W.class \
LDIV.class \
LLOAD.class \
LMUL.class \
LNEG.class \
LOOKUPSWITCH.class \
LOR.class \
LREM.class \
LRETURN.class \
LSHL.class \
LSHR.class \
LSTORE.class \
LSUB.class \
LUSHR.class \
LXOR.class \
LocalVariableGen.class \
LocalVariableInstruction.class \
MONITORENTER.class \
MONITOREXIT.class \
MULTIANEWARRAY.class \
MethodGen.class \
NEW.class \
NEWARRAY.class \
NOP.class \
POP.class \
POP2.class \
PUSH.class \
PUTFIELD.class \
PUTSTATIC.class \
RET.class \
RETURN.class \
SALOAD.class \
SASTORE.class \
SIPUSH.class \
SWAP.class \
SWITCH.class \
Select.class \
TABLESWITCH.class \
Type.class \
VariableLengthInstruction.class

all: $(FILES)

####################################################################

