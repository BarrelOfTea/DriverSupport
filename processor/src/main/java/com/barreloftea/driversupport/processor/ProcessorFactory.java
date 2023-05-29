package com.barreloftea.driversupport.processor;

public class ProcessorFactory {
    private static Processor processor = null;

    public static synchronized Processor getProcessor(){
        if (processor==null){
            processor = new Processor();
        }
        return processor;
    }
}
