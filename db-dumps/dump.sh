#!/bin/sh
pg_dump -h localhost -U postgres -W wil > `date +wil-%F.sql`

