package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl53
 * @date 01/01/2020
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        Validate.isTrue(value <= Float.MAX_VALUE,
                "Floatliteral values connot depass 2^128 - 2^104");
        Validate.isTrue(value >= Float.MIN_VALUE,
                "Floatliteral values connot be under  2^-{149}");
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (!compiler.getSymbols().checkSymbol("float")){
            throw new ContextualError("float Type is not yet implemented", this.getLocation());
        }
       Type returnType = new FloatType(compiler.getSymbols().getSymbol("float"));
       this.setType(returnType);
       return this.getType();       
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        GPRegister r = Register.getR(1);
        compiler.addInstruction(new LOAD(value, r));
        compiler.addInstruction(new WFLOAT());
    }
    public void codeGenLoad(DecacCompiler compiler, GPRegister r1){
        float val = this.getValue();
        compiler.addInstruction(new LOAD(val, r1));
    }

}
