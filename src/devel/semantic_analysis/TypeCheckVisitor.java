package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.And;
import core.abstract_syntax.syntaxtree.ArrayAssign;
import core.abstract_syntax.syntaxtree.ArrayLength;
import core.abstract_syntax.syntaxtree.ArrayLookup;
import core.abstract_syntax.syntaxtree.Assign;
import core.abstract_syntax.syntaxtree.Block;
import core.abstract_syntax.syntaxtree.BooleanType;
import core.abstract_syntax.syntaxtree.Call;
import core.abstract_syntax.syntaxtree.ClassDeclExtends;
import core.abstract_syntax.syntaxtree.ClassDeclSimple;
import core.abstract_syntax.syntaxtree.False;
import core.abstract_syntax.syntaxtree.Formal;
import core.abstract_syntax.syntaxtree.Identifier;
import core.abstract_syntax.syntaxtree.IdentifierExp;
import core.abstract_syntax.syntaxtree.IdentifierType;
import core.abstract_syntax.syntaxtree.If;
import core.abstract_syntax.syntaxtree.IntArrayType;
import core.abstract_syntax.syntaxtree.IntegerLiteral;
import core.abstract_syntax.syntaxtree.IntegerType;
import core.abstract_syntax.syntaxtree.LessThan;
import core.abstract_syntax.syntaxtree.MainClass;
import core.abstract_syntax.syntaxtree.MethodDecl;
import core.abstract_syntax.syntaxtree.Minus;
import core.abstract_syntax.syntaxtree.NewArray;
import core.abstract_syntax.syntaxtree.NewObject;
import core.abstract_syntax.syntaxtree.Not;
import core.abstract_syntax.syntaxtree.Plus;
import core.abstract_syntax.syntaxtree.Print;
import core.abstract_syntax.syntaxtree.Program;
import core.abstract_syntax.syntaxtree.This;
import core.abstract_syntax.syntaxtree.Times;
import core.abstract_syntax.syntaxtree.True;
import core.abstract_syntax.syntaxtree.Type;
import core.abstract_syntax.syntaxtree.VarDecl;
import core.abstract_syntax.syntaxtree.While;

public interface TypeCheckVisitor {
	public ProgramTable visit(Program n);
	public ClassTable visit(MainClass n);
	public ClassTable visit(ClassDeclSimple n);
	public ClassTable visit(ClassDeclExtends n);
	public Type visit(VarDecl n);
	public MethodTable visit(MethodDecl n);
	public Type visit(Formal n);
//  public Type visit(IntArrayType n);
//  public Type visit(BooleanType n);
//  public Type visit(IntegerType n);
//  public Type visit(IdentifierType n);
//  public void visit(Block n);
//  public void visit(If n);
//  public void visit(While n);
//  public void visit(Print n);
//  public void visit(Assign n);
//  public void visit(ArrayAssign n);
//  public void visit(And n);
//  public void visit(LessThan n);
//  public void visit(Plus n);
//  public void visit(Minus n);
//  public void visit(Times n);
//  public void visit(ArrayLookup n);
//  public void visit(ArrayLength n);
//  public void visit(Call n);
//  public void visit(IntegerLiteral n);
//  public void visit(True n);
//  public void visit(False n);
//  public void visit(IdentifierExp n);
//  public void visit(This n);
//  public void visit(NewArray n);
//  public void visit(NewObject n);
//  public void visit(Not n);
//  public void visit(Identifier n);
}
