package org.uengine.droolsintegration;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.metaworks.annotation.Face;
import org.metaworks.annotation.Range;
import org.uengine.kernel.*;
import org.uengine.kernel.face.SubProcessParameterContextArrayFace;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by uengine on 2017. 6. 27..
 */
public class CallDRoolActivity extends DefaultActivity {

    public CallDRoolActivity(){
        setName("Call Drool");
    }

    String knowledgeName;
    @Range(options={"CashFlowKS", "BussPassGoodKB"}, values={"CashFlowKS", "BussPassGoodKB"})
        public String getKnowledgeName() {
            return knowledgeName;
        }
        public void setKnowledgeName(String knowledgeName) {
            this.knowledgeName = knowledgeName;
        }


    ParameterContext[] variableBindings;
    @Face(
            faceClass = SubProcessParameterContextArrayFace.class
    )
        public ParameterContext[] getVariableBindings() {
            return this.variableBindings;
        }
        public void setVariableBindings(ParameterContext[] contexts) {
            this.variableBindings = contexts;
        }


    @Override
    protected void executeActivity(ProcessInstance processInstance) throws Exception {

        KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
        KieSession ksession = kc.newKieSession( getKnowledgeName());


        for(ParameterContext parameterContext : getVariableBindings()){
            if(parameterContext.getDirection() != ParameterContext.DIRECTION_OUT){

                ProcessVariableValue value = parameterContext.getVariable().getMultiple(processInstance, "");

                do{
                    ksession.insert(value.getValue());
                }while(value.next());

            }
        }

        ksession.fireAllRules();

        Collection<? extends Object> objects = ksession.getObjects();


        for(Object object : objects){
            for(ParameterContext parameterContext : getVariableBindings()){

                Class variableType = getProcessDefinition().getProcessVariable(parameterContext.getVariable().getName()).createNewValue().getClass();

                if(variableType.isAssignableFrom(object.getClass()) && parameterContext.getDirection() != ParameterContext.DIRECTION_IN){
                    parameterContext.getVariable().set(processInstance, "", (Serializable) object);
                }
            }
        }

        fireComplete(processInstance);
    }
}
