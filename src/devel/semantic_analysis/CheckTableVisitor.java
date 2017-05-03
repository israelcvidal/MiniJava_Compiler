package devel.semantic_analysis;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

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
import core.abstract_syntax.syntaxtree.Exp;
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
import core.abstract_syntax.syntaxtree.Statement;
import core.abstract_syntax.syntaxtree.This;
import core.abstract_syntax.syntaxtree.Times;
import core.abstract_syntax.syntaxtree.True;
import core.abstract_syntax.syntaxtree.Type;
import core.abstract_syntax.syntaxtree.VarDecl;
import core.abstract_syntax.syntaxtree.While;
import core.abstract_syntax.visitor.TypeVisitor;
import core.abstract_syntax.visitor.Visitor;

/**
 * This class checks the table built by BuildTableVisitor in order to check if variables and methods used were declared and
 * have the correct type.  
 * @author Israel
 */

public class CheckTableVisitor implements TypeVisitor{
	private ProgramTable programTable;
	private ClassTable currenClass=null;
	private MethodTable currenMethod=null;
//	private MethodTable currenMethod=null;
	public CheckTableVisitor(ProgramTable pt) {
		this.programTable = pt;
		
	}
	@Override
	public Type visit(Program n) {
		n.m.accept(this);
	
		for (int i = 0; i < n.cl.size(); i++) {
			ClassDecl cd = n.cl.elementAt(i);
			cd.accept(this);
		}
			
		return null;
	}
	@Override
	public Type visit(MainClass n) {
		this.currenClass = this.programTable.getClass(Symbol.symbol(n.i1.s));
		n.s.accept(this);
	
		return null;
	}
	
	@Override
	public Type visit(ClassDeclSimple n) {
		this.currenClass = this.programTable.getClass(Symbol.symbol(n.i.s));

		for (int i = 0; i < n.ml.size(); i++) {
			MethodDecl md = n.ml.elementAt(i);
			md.accept(this);
		}
		
		return null;
	}
	@Override
	public Type visit(ClassDeclExtends n) {
		this.currenClass = this.programTable.getClass(Symbol.symbol(n.i.s));
		
		for (int i = 0; i < n.ml.size(); i++) {
			MethodDecl md = n.ml.elementAt(i);
			md.accept(this);
		}
		
		return null;
	}
	@Override
	public Type visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(MethodDecl n) {
		this.currenMethod = this.currenClass.getMethod(Symbol.symbol(n.i.toString()));
		
		for (int i = 0; i < n.sl.size(); i++) {
			Statement st = n.sl.elementAt(i);
			st.accept(this);
		}
		n.e.accept(this);
		
		return null;
	}
	@Override
	public Type visit(Formal n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(IdentifierType n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Block n) {
		for (int i = 0; i < n.sl.size(); i++) {
			Statement st = n.sl.elementAt(i);
			st.accept(this);
		}

		return null;
	}
	@Override
	public Type visit(If n) {
		n.e.accept(this);
		n.s1.accept(this);
		n.s2.accept(this);

		return null;
	}
	@Override
	public Type visit(While n) {
		n.e.accept(this);
		n.s.accept(this);
		
		return null;
	}
	@Override
	public Type visit(Print n) {
		n.e.accept(this);
		
		return null;
	}
	@Override
	public Type visit(Assign n) {
		Type expType = n.e.accept(this);
		
		Type identifierType = this.currenMethod.getLocal(Symbol.symbol(n.i.s));
		try{
			
			if(identifierType == null){
				identifierType = this.currenMethod.getFormal(Symbol.symbol(n.i.s));
				if(identifierType == null){
					identifierType = this.currenClass.getField(Symbol.symbol(n.i.s));
					if(identifierType == null){
						throw new SemanticErrorException("id '" + n.i.s + "' does not exist");
					}
				}
			}
			
			if( (identifierType instanceof IntegerType) && (expType instanceof IntegerType) ){
				
			}
			
		} catch(SemanticErrorException see){
			see.printStackTrace();
		}
		return null;
	}
	@Override
	public Type visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(True n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(False n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Not n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	  
	

}