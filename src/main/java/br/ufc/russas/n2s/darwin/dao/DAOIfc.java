package br.ufc.russas.n2s.darwin.dao;

import java.util.List;
import org.hibernate.SessionFactory;

/**
 *
 * @author Wallison Carlos
 * @param <T>
 */
public interface DAOIfc<T> {

    /**
     *
     * @return SessionFactory
     */
	public SessionFactory getSessionFactory();

    /**
     * Método resposável por fazer a persistência de um objeto qualquer,
     * com anotações hibernate.
     * @param object
     * obejct - um objeto qualquer que contenha anotações Hibernate
     * @return T
     * T - objeto genérico que assumi a forma da classe que está sendo
     * persistida, que contém anotações Hibernate.
     */
	public T adiciona(T object);

    /**
     * Método resposável por fazer a atualização de um objeto qualquer,
     * com anotações Hibernate.
     * @param object
     * obejct - um objeto qualquer que contenha anotações Hibernate
     * @return T
     * T - objeto genérico que assumi a forma da classe que está sendo
     * atualizada, que contém anotações Hibernate.
     */
	public T atualiza(T object);

    /**
     * Método resposável por fazer a remoção de um objeto qualquer,
     * com anotações Hibernate.
     * @param object
     * obejct - um objeto qualquer que contenha anotações Hibernate
     */
	public void remove(T object);

    /**
     * Método resposável por retornar um Criteria básico de todas os objetos
     * de uma determinada classe com anotações Hibernate.
     * @param object
     * obejct - do tipo Class, recebe uma classe que seja anotada com Hibernate
     * @return Criteria
     * List<T> -  lista com os objetos da classe que se deseja obter as
     * instâncias.
     */
	public List<T> lista(T object);

    /**
     * Método resposável por pegar do banco de dados uma objeto que contém
     * anotações Hibernate, a partir do código informado.
     * @param classe
     * classe - do tipo Class, recebe uma classe que seja anotada com Hibernate
     * @param codObject
     * codObject - do tipo long que representa o código do objeto no qual se
     * deseja recuperar
     * @return T
     * T - objeto genérico que pode assumir a forma de uma classe anotada com
     * Hibernate
     */
	public T getObject(T object, long codObject);
    
}
