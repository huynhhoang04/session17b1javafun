
CREATE TABLE IF NOT EXISTS movies (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    director VARCHAR(255) NOT NULL,
    release_year INT NOT NULL
);


CREATE OR REPLACE PROCEDURE add_movie(
    p_title VARCHAR, 
    p_director VARCHAR, 
    p_year INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO movies (title, director, release_year) 
    VALUES (p_title, p_director, p_year);
END;
$$;


CREATE OR REPLACE PROCEDURE update_movie(
    p_id INT,
    p_title VARCHAR, 
    p_director VARCHAR, 
    p_year INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE movies 
    SET title = p_title, 
        director = p_director, 
        release_year = p_year
    WHERE id = p_id;
END;
$$;


CREATE OR REPLACE PROCEDURE delete_movie(p_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM movies WHERE id = p_id;
END;
$$;


CREATE OR REPLACE FUNCTION list_movies()
RETURNS TABLE (
    out_id INT,
    out_title VARCHAR,
    out_director VARCHAR,
    out_year INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY SELECT id, title, director, release_year FROM movies ORDER BY id;
END;
$$;
