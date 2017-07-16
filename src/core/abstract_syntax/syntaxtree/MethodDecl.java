package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.Stm;
import devel.IR_translation.IRVisitor;
import devel.semantic_analysis.MethodTable;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class MethodDecl {
  public Type t;
  public Identifier i;
  public FormalList fl;
  public VarDeclList vl;
  public StatementList sl;
  public Exp e;

  public MethodDecl(Type at, Identifier ai, FormalList afl, VarDeclList avl, 
                    StatementList asl, Exp ae) {
    t=at; i=ai; fl=afl; vl=avl; sl=asl; e=ae;
  }
 
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public MethodTable accept(TypeCheckVisitor v) {
	  return v.visit(this);
  }
  
  public Stm accept(IRVisitor v) {
	  return v.visit(this);
  }
  
}
