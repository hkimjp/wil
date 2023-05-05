#!/bin/sh
pg_dump -h localhost -U postgres wil > `date +wil-%F.sql`
