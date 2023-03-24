# Super plan

## First session: first steps

- [x] cloned the project
- [x] made a circle that bounces

## Second session: single jumpy ball

- [x] change something on border collision
- [x] change velocity when pressing keyboard buttons
- [x] store velocity as a individual property of the circle

## Third session: multiple jumpy balls

- [x] instantiate circles properly
- [x] Ideal Gas route
    - [x] add multiple circles
    - [x] have a way to store velocities of circles of different colors

## Fourth session: adding and removing ~balls~ particles on demand

- [x] bugfix directions
- [x] have a keypress that adds a particle to the box
- [x] have a keypress that deletes a particle from the box

## Fifth session: 2d gas particles movement

- [x] introduce horizontal velocity and bounce of sides
- [x] maybe bounce particles from each other as well
- [x] Ideal Gas route
  - [x] start colliding
  - [x] direction as vector

## Sixth session: "More" realistic collisions

- [x] add masses
- [x] do the elastic collision
- [x] collide with walls non-elastically
- [x] account for ~size~ mass difference when colliding

## Seventh session: FIX COLLISIONS FOR GOOOOOD

- [x] fix collisions to make them account for Gas Particle centers
- [x] have a text with energy inside the scene (some sort of UI)
- [x] Don't get stuck in the walls
- [x] Fix annoying color palette
- [x] Fix the bug with speedup

## Ninth session:

- [x] Attach speed vectors to particles

## Next
- [ ] Show them on click
- [ ] Replace Line with Rect
- [ ] Debug weird collisions

## Next-next

- [x] possibility to move left paddle
- [x] sneaky possibility to move right paddle
- [x] make the ball bounce from the paddles
- [x] make the ball bounce from ceil/floor
- [X] simple pong AI
- [ ] AAA like SLOWDOWN ON LOOOSE !!!!!!
- [ ] Fancy effects like particles

## Multiplayer pong detour

- [ ] game state
- [ ] A "client" that renders the field
- [ ] A proper "server" that can wait for connections
- [ ] 3 Scenes and changes between them:
  - [ ] start selection Local/Host/Client
  - [ ] second with game itself
  - [ ] third Win / Loose graphics and score
  - [ ] UI yes we want UI please add UI
- [ ] Maybe implement tetris instead of pong

## Brave New World

- [ ] Implement 2-body/3-body problem (no collisions)
- [ ] Use Box2D (plugin for physics in korge)
- [ ] Make a platformer in Korge
- [ ] Simple game in Korge with SOME backend (+1 from Igor)
- [ ] Take a look at Kotlin-godot?
- [ ] velocity-based color?
   - [ ] check dataclasses
   - [ ] make benchmarks, probably runtime-dependent
- [ ] Continue with one ball
   - [ ] add gravity or some sort of acceleration
   - [ ] change something else like add air friction
- [ ] Add joystick


## MULTIPONG THOUGHTS

- [x] CONNECT BUTTON for client
- [ ] Some ui for networking stuff maybe
- [ ] ACTUAL multiplayer against each other (sending ip address), possibly hack it around. JUST ONCE
- [ ] PARTICLE EFFECTSS for the BAAALL
