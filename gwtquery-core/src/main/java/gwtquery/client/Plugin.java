package gwtquery.client;

/**
 * A GQuery plugin
 */
public interface Plugin<T extends GQuery> {
     T init(GQuery gq);
}
