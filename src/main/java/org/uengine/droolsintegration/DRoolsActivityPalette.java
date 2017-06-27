package org.uengine.droolsintegration;

import org.springframework.stereotype.Component;
import org.uengine.kernel.SQLActivity;
import org.uengine.modeling.CompositePalette;
import org.uengine.modeling.Palette;
import org.uengine.modeling.PaletteWindow;
import org.uengine.modeling.modeler.palette.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uengine on 2017. 6. 27..
 */
@Component
public class DRoolsActivityPalette extends BPMNPalette {
    public DRoolsActivityPalette() {
        super();


        CompositePalette bpmnPaletteWindow = new PaletteWindow();

        /**
         * BPMN
         */
        bpmnPaletteWindow.addPalette(new EventPalette());

        final TaskPalette taskPalette = new TaskPalette();
        taskPalette.addSymbol(new CallDRoolActivity().createView().createSymbol());
        taskPalette.addSymbol(new SQLActivity().createView().createSymbol());

        bpmnPaletteWindow.addPalette(taskPalette);
        bpmnPaletteWindow.addPalette(new GatewayPalette());

        bpmnPaletteWindow.setName("Palette");

        /**
         * processVariablePalette
         */
        setProcessVariablePalette(new ProcessVariablePalette());

        List<Palette> palettes = new ArrayList<>();
        palettes.add(bpmnPaletteWindow);
        palettes.add(getProcessVariablePalette());

        setChildPalettes(palettes);
    }
}
