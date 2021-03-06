/*
 * @(#)$Id: AssignAST.java,v 1.2 2004/04/02 05:48:47 james Exp $
 *
 * JParse: a freely available Java parser
 * Copyright (C) 2000,2004 Jeremiah W. James
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Author: Jerry James
 * Email: james@eecs.ku.edu, james@ittc.ku.edu, jamesj@acm.org
 * Address: EECS Department - University of Kansas
 *          Eaton Hall
 *          1520 W 15th St, Room 2001
 *          Lawrence, KS  66045-7621
 */
package jparse.expr;

import antlr.Token;
import jparse.JavaTokenTypes;
import jparse.Type;
import jparse.VarList;

/**
 * An AST node that represents an assignment
 *
 * @version $Revision: 1.2 $, $Date: 2004/04/02 05:48:47 $
 * @author Jerry James
 */
public final class AssignAST extends ExpressionAST implements JavaTokenTypes {

    /**
     * The left-hand side of the assignment
     */
    private ExpressionAST lhs;

    /**
     * The right-hand side of the assignment
     */
    private ExpressionAST rhs;

    /**
     * Create a new assignment AST
     *
     * @param token the token represented by this AST node
     */
    public AssignAST(final Token token) {
	super(token);
    }

    public void parseComplete() {
	lhs = (ExpressionAST)getFirstChild();
	rhs = (ExpressionAST)lhs.getNextSibling();
	lhs.parseComplete();
	rhs.parseComplete();
    }

    protected Type computeType() {
	// The type of an assignment is the type of its left-hand side
	final Type type = lhs.retrieveType();
	if (type == Type.stringType && getType() == PLUS_ASSIGN)
	    setType(CONCAT_ASSIGN);
	return type;
    }

    protected Type[] computeExceptions() {
	// The lhs of an assignment can't throw any checked exceptions
	return rhs.getExceptionTypes();
    }

    protected Object computeValue() {
	return rhs.getValue();
    }

    public VarList getVarList() {
	final VarList leftList = lhs.getVarList();
	final VarList rightList = rhs.getVarList();
	return new VarList(leftList, rightList, true);
    }

    /**
     * Get the left-hand-side of this assignment expression
     *
     * @return the lhs of the expression
     */
    public ExpressionAST getLeft() {
	return lhs;
    }

    /**
     * Get the right-hand-side of this assignment expression
     *
     * @return the rhs of the expression
     */
    public ExpressionAST getRight() {
	return rhs;
    }
}
