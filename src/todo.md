# Super plan

## First session: first steps

[x] cloned the project
[x] made a circle that bounces

## Second session: single jumpy ball

[x] change something on border collision
[x] change velocity when pressing keyboard buttons
[x] store velocity as a individual property of the circle

## Third session: multiple jumpy balls

[x] instantiate circles properly
[~] Ideal Gas route
    [x] add multiple circles
    [x] have a way to store velocities of circles of different colors

## Fourth session: adding and removing ~balls~ particles on demand

[x] bugfix directions
[x] have a keypress that adds a particle to the box
[x] have a keypress that deletes a particle from the box

## Fifth session: 2d gas particles movement

[x] introduce horizontal velocity and bounce of sides
[x] maybe bounce particles from each other as well
[x] Ideal Gas route
    [x] start colliding
    [x] direction as vector

## Sixth session: "More" realistic collisions

[x] add masses
[x] do the elastic collision
[x] collide with walls non-elastically
[x] account for ~size~ mass difference when colliding

## Brave New World

[ ] have a text with velocity inside the scene (some sort of UI)
[ ] Fix annoying color palette
[ ] Fix the bug with speedup
[ ] consider vector2D from this [gist](https://gist.github.com/RazorNd/3b8d3b906514bcd60055020efb8e8eb2)
[ ] velocity-based color?
    [ ] check dataclasses
    [ ] make benchmarks, probably runtime-dependent
[ ] Continue with one ball
    [ ] add gravity or some sort of acceleration
    [ ] change something else like add air friction
[ ] Two-body problem? Three-body problems?
[ ] Make a platformer
[ ] Add joystick
