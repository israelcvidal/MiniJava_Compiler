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
		
		return new IntArrayType();
	}
	@Override
	public Type visit(BooleanType n) {

		return new BooleanType();
	}
	@Override
	public Type visit(IntegerType n) {
	
		return new IntegerType();
	}
	@Override
	public Type visit(IdentifierType n) {
		Type idType = null;
		try{
			idType = this.currenMethod.getLocal(Symbol.symbol(n.s));
			if(idType == null){
				idType = this.currenMethod.getFormal(Symbol.symbol(n.s));
				if(idType == null){
					idType = this.currenClass.getField(Symbol.symbol(n.s));
					if(idType == null){
						throw new SemanticErrorException("id '" + n.s + "' does not exist");
					}
				}
			}
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		return idType;
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
		Type idType = n.i.accept(this);
		
		try{
				
			if( !((idType instanceof IntegerType) && (expType instanceof IntegerType)) ){
				if( !((idType instanceof BooleanType) && (expType instanceof BooleanType)) ){
					if( !((idType instanceof IntArrayType) && (expType instanceof IntArrayType)) ){
						throw new SemanticErrorException(n.i.s + " and " + n.e + " must have same type to assign");
					}
				}
			}
		
		} catch(SemanticErrorException see){
			see.printStackTrace();
		}
		return null;
	}
	@Override
	public Type visit(ArrayAssign n) {
		Type idType = n.i.accept(this);
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(idType instanceof IntArrayType)){
				throw new SemanticErrorException(n.i.s + "must be IntArrayType");
			}
			if( !(exp1Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e1 + "must be IntType");
			}
			
			if (!(exp2Type instanceof IntegerType) ){
				throw new SemanticErrorException(n.e2 + "must be IntType");
			}
						
		} catch(SemanticErrorException see){
			see.printStackTrace();
		}
		return null;
	}
	@Override
	public Type visit(And n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(exp1Type instanceof BooleanType)){
				throw new SemanticErrorException(n.e1 + "must be BooleanType");
			}
			
			if(!(exp2Type instanceof BooleanType)){
				throw new SemanticErrorException(n.e2 + "must be BooleanType");
			}
			
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		
		return new BooleanType();
	}
	@Override
	public Type visit(LessThan n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(exp1Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e1 + "must be IntegerType");
			}
			
			if(!(exp2Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e2 + "must be IntegerType");
			}
			
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		
		return new BooleanType();
	}
	@Override
	public Type visit(Plus n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(exp1Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e1 + "must be IntegerType");
			}
			
			if(!(exp2Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e2 + "must be IntegerType");
			}
			
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		
		return new IntegerType();
	}
	@Override
	public Type visit(Minus n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(exp1Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e1 + "must be IntegerType");
			}
			
			if(!(exp2Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e2 + "must be IntegerType");
			}
			
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		
		return new IntegerType();
	}
	@Override
	public Type visit(Times n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(exp1Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e1 + "must be IntegerType");
			}
			
			if(!(exp2Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e2 + "must be IntegerType");
			}
			
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		
		return new IntegerType();
	}
	@Override
	public Type visit(ArrayLookup n) {
		Type exp1Type = n.e1.accept(this);
		Type exp2Type = n.e2.accept(this);
		
		try{
			if(!(exp1Type instanceof IntArrayType)){
				throw new SemanticErrorException(n.e1 + "must be IntArrayType");
			}
			if(!(exp2Type instanceof IntegerType)){
				throw new SemanticErrorException(n.e2 + "must be IntegerType");
			}

			
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}

		return new IntegerType();
	}
	@Override
	public Type visit(ArrayLength n) {
		Type expType = n.e.accept(this);
		
		try{
			if( !(expType instanceof IntArrayType)){
				throw new SemanticErrorException(n.e + "must be IntArrayType");
			}
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		return new IntegerType();
	}
	@Override
	public Type visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Type visit(IntegerLiteral n) {
		
		return new IntegerType();
	}
	@Override
	public Type visit(True n) {

		return new BooleanType();
	}
	@Override
	public Type visit(False n) {
		
		return new BooleanType();
	}
	@Override
	public Type visit(IdentifierExp n) {
		Type idType = null;
		try{
			idType = this.currenMethod.getLocal(Symbol.symbol(n.s));
			if(idType == null){
				idType = this.currenMethod.getFormal(Symbol.symbol(n.s));
				if(idType == null){
					idType = this.currenClass.getField(Symbol.symbol(n.s));
					if(idType == null){
						throw new SemanticErrorException("id '" + n.s + "' does not exist");
					}
				}
			}
		}catch(SemanticErrorException see){
			see.printStackTrace();
		}
		return idType;
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