package pl.edu.pw.mwoproj.mappers;

public interface EntityMapper<E, D> {
    D mapToDto(E entity);
    E mapToEntity(D dto);
}
