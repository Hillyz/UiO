-- Oppgave 1
SELECT CONCAT(p.firstname, ' ', p.lastname) AS actor, fc.filmcharacter
FROM film AS f
INNER JOIN filmparticipation AS fp using (filmid)
INNER JOIN person AS p using (personid)
INNER JOIN filmcharacter AS fc using (partid)
WHERE f.title = 'Star Wars' AND fp.parttype = 'cast';

-- Oppgave 2
select c.country, count(fc.filmid) as antallfilmer
from country as c
inner join filmcountry as fc using (country)
group by c.country
;

-- Oppgave 3 work in progress
-- 60 rader i stedet for 44
select rt.country, avg(cast(rt.time as int))
from runningtime as rt
where rt.time ~ '^\d+$' and rt.country is not null
group by rt.country
having count(rt.time) >= 200
;

-- Oppgave 4
select f.title, count(fg.genre) as antallsjangere
from film as f
inner join filmgenre as fg on (fg.filmid = f.filmid)
inner join filmitem as fi on (fi.filmid = f.filmid)
where fi.filmtype = 'C'
group by f.title
order by antallsjangere desc
limit 10
;

-- Oppgave 5
select country, count(filmid) as antallfilmer, avg(rank) as avg_rating
from (
    select fc.country, fc.filmid, fr.rank, fg.genre
    from filmcountry as fc
    inner join filmrating as fr using (filmid)
    inner join filmgenre as fg using (filmid)
) as t
group by country
;
-- Land | totalt antall filmer | avg rating | max (mest vanlig) sjanger


