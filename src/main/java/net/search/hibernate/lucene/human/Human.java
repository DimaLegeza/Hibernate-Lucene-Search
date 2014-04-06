package net.search.hibernate.lucene.human;

import org.apache.solr.analysis.*;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author: Dima Legeza
 * @since: 06.04.14
 */
@Entity
@Indexed
@AnalyzerDef(name = "synonymsAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = SynonymFilterFactory.class, params = {
                        @Parameter(name = "synonyms", value = "net/search/hibernate/lucene/dictionary/synonyms.txt"),
                        @Parameter(name = "ignoreCase", value = "true"),
                        @Parameter(name = "expand", value = "false")}
                )
})
public class Human {

    @Id
    @GeneratedValue
    @DocumentId
    private Long id;

    @Column
    @Field(store = Store.YES)
    private String name;

    @Column
    @Field(store = Store.YES)
    private String surname;

    @Column
    @Field(store = Store.YES)
    private short birthYear;

    @Column
    @Field(store = Store.NO)
    private String gender;

    public Human() {
    }

    public Human(String name, String surname, short birthYear, String gender) {
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public short getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(short birthYear) {
        this.birthYear = birthYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
