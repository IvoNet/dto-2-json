package nl.ivonet.dto;

public class ProcessingData<T extends DataCommon> {

    private final T data;
    private final Rich rich;

    public ProcessingData(final T data, final Rich rich) {
        this.data = data;
        this.rich = rich;
    }

    public T getData() {
        return this.data;
    }

    public Rich getRich() {
        return this.rich;
    }
}
