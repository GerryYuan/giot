package org.giot.core.network;

/**
 * Topic/Processor
 *
 * @author Created by gerry
 * @date 2021-03-18-10:16 PM
 */
public interface ProcessorMapping {

    <T extends Source> SourceProcessor getProcessor(T source);
}
