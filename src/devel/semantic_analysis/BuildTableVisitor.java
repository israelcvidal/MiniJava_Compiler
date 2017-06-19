package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.ClassDecl;
import core.abstract_syntax.syntaxtree.ClassDeclExtends;
import core.abstract_syntax.syntaxtree.ClassDeclSimple;
import core.abstract_syntax.syntaxtree.Formal;
import core.abstract_syntax.syntaxtree.MainClass;
import core.abstract_syntax.syntaxtree.MethodDecl;
import core.abstract_syntax.syntaxtree.Program;
import core.abstract_syntax.syntaxtree.Type;
import core.abstract_syntax.syntaxtree.VarDecl;

/**
 * The class implements analysis semantic's first pass.
 * @author daniel
 *
 */

public class BuildTableVisitor implements TypeCheckVisitor {
	@Override
	public ProgramTable visit(Program n) {
		ProgramTable result = new ProgramTable();
		
		try {
			result.putClass(Symbol.symbol(n.m.i1.s), n.m.accept(this));
			
			for (int i = 0; i < n.cl.size(); i++) {
				ClassDecl cd = n.cl.elementAt(i);
				
				if (cd instanceof ClassDeclSimple) {
					ClassDeclSimple cds = (ClassDeclSimple) cd;
					
					result.putClass(Symbol.symbol(cds.i.s), cds.accept(this));
				}
				else {
					ClassDeclExtends cde = (ClassDeclExtends) cd;
					
					result.putClass(Symbol.symbol(cde.i.s), cde.accept(this));
					
					ClassTable parentClassTable = result.getClass(Symbol.symbol(cde.j.s));
					
					result.getClass(Symbol.symbol(cde.i.s)).setParentClass(parentClassTable);
					
				}
				
			}
			
		} catch (SemanticErrorException see) {
			see.printStackTrace();	
		}
		
		return result;
	}

	@Override
	public ClassTable visit(MainClass n) {
		ClassTable result = new ClassTable(n.i1.s);
		
		return result;
	}

	@Override
	public ClassTable visit(ClassDeclSimple n) {
		ClassTable result = new ClassTable(n.i.s);
		
		try {
			for (int i = 0; i < n.vl.size(); i++) {
				VarDecl vd = n.vl.elementAt(i);
				
				result.putField(Symbol.symbol(vd.i.s), vd.accept(this));
				
			}
			
			for (int i = 0; i < n.ml.size(); i++) {
				MethodDecl md = n.ml.elementAt(i);
				
				result.putMethod(Symbol.symbol(md.i.s), md.accept(this));
			}
			
		} catch (SemanticErrorException see) {
			see.printStackTrace();	
		}
		
		return result;
	}

	@Override
	public ClassTable visit(ClassDeclExtends n) {
		ClassTable result = new ClassTable(n.i.s);
		
		try {			
			for (int i = 0; i < n.vl.size(); i++) {
				VarDecl vd = n.vl.elementAt(i);
				
				result.putField(Symbol.symbol(vd.i.s), vd.accept(this));
			}
			
			for (int i = 0; i < n.ml.size(); i++) {
				MethodDecl md = n.ml.elementAt(i);
				
				result.putMethod(Symbol.symbol(md.i.s), md.accept(this));
			}
			
		} catch (SemanticErrorException see) {
			see.printStackTrace();	
		}
		
		return result;
	}

	@Override
	public Type visit(VarDecl n) {
		return n.t;
	}

	@Override
	public MethodTable visit(MethodDecl n) {
		MethodTable result = new MethodTable(n.i.s, n.t);
		
		try {
			for (int i = 0; i < n.fl.size(); i++) {
				Formal f = n.fl.elementAt(i);
				
				result.putFormal(Symbol.symbol(f.i.s), f.accept(this));
			}
			
			for (int i = 0; i < n.vl.size(); i++) {
				VarDecl vd = n.vl.elementAt(i);
				
				result.putLocal(Symbol.symbol(vd.i.s), vd.accept(this));
			}
			
		} catch (SemanticErrorException see) {
			see.printStackTrace();	
		}
		
		return result;
	}

	@Override
	public Type visit(Formal n) {
		return n.t;
	}
	
}
