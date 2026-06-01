DROP TABLE IF EXISTS inscriere;
DROP TABLE IF EXISTS curs;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS instructor;
DROP TABLE IF EXISTS categorie;

CREATE TABLE categorie (
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    nume TEXT NOT NULL
);

CREATE TABLE instructor (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    username    TEXT NOT NULL,
    email       TEXT,
    parola      TEXT NOT NULL,
    departament TEXT
);

CREATE TABLE student (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    email    TEXT,
    parola   TEXT NOT NULL,
    grupa    TEXT
);

CREATE TABLE curs (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    titlu            TEXT NOT NULL,
    categorie_id     INTEGER NOT NULL,
    instructor_id    INTEGER NOT NULL,
    numar_inscrieri  INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (categorie_id) REFERENCES categorie(id),
    FOREIGN KEY (instructor_id) REFERENCES instructor(id)
);

CREATE TABLE inscriere (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id      INTEGER NOT NULL,
    curs_id         INTEGER NOT NULL,
    data_inscriere  TEXT NOT NULL,
    activ           INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (curs_id) REFERENCES curs(id)
);
