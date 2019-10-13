docker exec -i codenjoy-database psql -U codenjoy -c "SELECT time, score, save FROM saves WHERE time LIKE '$1%' ORDER BY score DESC;"
