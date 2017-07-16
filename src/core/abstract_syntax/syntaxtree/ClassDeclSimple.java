package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import core.translation_to_IR.tree.Stm;
import devel.IR_translation.IRVisitor;
import devel.semantic_analysis.ClassTable;
import devel.semantic_analysis.TypeCheckVisitor;
import core.abstract_syntax.visitor.TypeVisitor;

public class ClassDeclSimple extends ClassDecl {
  public Identifier i;
  public VarDeclList vl;  
  public MethodDeclList ml;
 
  public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml) {
    i=ai; vl=avl; ml=aml;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public ClassTable accept(TypeCheckVisitor v) {
	  return v.visit(this);
  }

	@Override
	public Stm accept(IRVisitor v) {
		return v.visit(this);
	}

}
