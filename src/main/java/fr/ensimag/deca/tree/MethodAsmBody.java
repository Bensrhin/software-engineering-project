package fr.ensimag.deca.tree;
import java.util.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.Label;

/**
 * @author gl53
 * @date 01/01/2020
 */
public class MethodAsmBody extends Tree {
    private static final Logger LOG = Logger.getLogger(MethodAsmBody.class);

    AbstractStringLiteral code;
    public MethodAsmBody(AbstractStringLiteral code) {
        Validate.notNull(code);
        this.code = code;
    }


    protected void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv,
              ClassDefinition currentClass) throws ContextualError
      {
        code.verifyExpr(compiler, localEnv, currentClass);
      }

    protected void codeGenBody(DecacCompiler compiler) {
        //
    }

    @Override
    public void decompile(IndentPrintStream s) {

        code.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        code.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        code.prettyPrint(s, prefix, true);
    }

}
