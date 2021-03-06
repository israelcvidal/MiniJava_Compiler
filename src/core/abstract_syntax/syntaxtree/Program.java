package core.abstract_syntax.syntaxtree;

import core.abstract_syntax.visitor.Visitor;
import devel.IR_translation.Frag;
import devel.IR_translation.IRVisitor;
import devel.semantic_analysis.ProgramTable;
import devel.semantic_analysis.TypeCheckVisitor;

import java.util.List;

import core.abstract_syntax.visitor.TypeVisitor;

public class Program {
  public MainClass m;
  public ClassDeclList cl;

  public Program(MainClass am, ClassDeclList acl) {
    m=am; cl=acl; 
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  public ProgramTable accept(TypeCheckVisitor v) {
	  return v.visit(this);
  }
  
  public List<Frag> accept(IRVisitor v) {
	  return v.visit(this);
  }
  
}
