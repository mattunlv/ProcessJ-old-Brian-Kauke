package CodeGeneratorC;

import AST.*;
import Utilities.*;
import Utilities.Error;

public class CodeGenerator<T extends AST> extends Visitor<T> {
    public static SymbolTable symtab = new SymbolTable();

    public CodeGenerator() {
    	Log.log("======================================");
    	Log.log("* C O D E   G E N E R A T O R  ( C )  *");
    	Log.log("======================================");
    }

    // AltCase
    // AltStat
    // ArrayAccessExpr
    // ArrayLiteral
    // ArrayType
    // Assignment
    // BinaryExpr
    // Block
    // BreakStat
    // CastExpr
    // ChannelType
    // ChannelEndExpr
    // ChannelEndType
    // ChannelReadExpr
    // ChannelWriteStat
    // ClaimStat
    // Compilation
    // ConstantDecl
    // ContinueStat
    // DoStat
    // ExprStat
    // ExternType
    // ForStat
    // Guard
    // IfStat
    // Import
    // Invocation
    // LocalDecl
    // Modifier
    // Name
    // NamedType
    // NameExpr
    // NewArray
    // NewMobile
    // ParamDecl
    // ParBlock
    // Pragma
    // PrimitiveLiteral
    // PrimitiveType
    // ProcTypeDecl
    // ProtocolLiteral
    // ProtocolCase
    // ProtocolTypeDecl
    // RecordAccess
    // RecordLiteral
    // RecordMember
    // RecordTypeDecl
    // ReturnStat
    // Sequence
    // SkipStat
    // StopStat
    // SuspendStat
    // SwitchGroup
    // SwitchLabel
    // SwitchStat
    // SyncStat
    // Ternary
    // TimeoutStat
    // UnaryPostExpr
    // UnaryPreExpr
    // Var
    // WhileStat
    public T visitWhileStat(WhileStat ws) {
	super.visitWhileStat(ws);
	return null;
    }
}