package devel.semantic_analysis;

import core.abstract_syntax.syntaxtree.And;
import core.abstract_syntax.syntaxtree.ArrayAssign;
import core.abstract_syntax.syntaxtree.ArrayLength;
import core.abstract_syntax.syntaxtree.ArrayLookup;
import core.abstract_syntax.syntaxtree.Assign;
import core.abstract_syntax.syntaxtree.Block;
import core.abstract_syntax.syntaxtree.BooleanType;
import core.abstract_syntax.syntaxtree.Call;
import core.abstract_syntax.syntaxtree.ClassDecl;
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
		MethodTable result = new MethodTable(n.t);
		
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

//	@Override
//	public Type visit(IntArrayType n) {
//		return n;
//	}
//
//	@Override
//	public Type visit(BooleanType n) {
//		return n;
//	}
//
//	@Override
//	public Type visit(IntegerType n) {
//		return n;
//	}
//
//	@Override
//	public Type visit(IdentifierType n) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public void visit(Block n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(If n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(While n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Print n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Assign n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(ArrayAssign n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(And n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(LessThan n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Plus n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Minus n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Times n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(ArrayLookup n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(ArrayLength n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Call n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(IntegerLiteral n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(True n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(False n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(IdentifierExp n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(This n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(NewArray n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(NewObject n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Not n) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void visit(Identifier n) {
//		// TODO Auto-generated method stub
//		
//	}
//	
}
