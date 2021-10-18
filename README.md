# Tiling by dominoes of a polygon

The project I made for the end of my double bachelor's degree in mathematics and computer science.
This is an implementation of the tiling algorithm of a polygon by dominoes in the plane. Described the first time in [[2]](J.C), the implementation is based on the paper of E. Rémilia [[1]](E.R). The GUI was handmade and the important algorithms are in the [algorithms directory](https://github.com/YellowWaitt/domino-tiling/tree/main/src/application/model/algorithms).

### The tiling algorithm

To generate a tiling, the algorithm use a bijection between the set of tiling of a polygon and a set of height function defined on the polygon. The term of height function comes from [[2]](J.C). Hence the algorithm try to build a well defined height function given a polygon and the tiling is constructed afterwards. If the algorithm fail to build a well defined height function, this tell that the polygon is non tilleable. All the mathematical details can be found in  [[1]](E.R). 

| A polygon | A height function |
| --- | --- |
| ![](https://github.com/YellowWaitt/domino-tiling/blob/main/images/polygon.png) | ![](https://github.com/YellowWaitt/domino-tiling/blob/main/images/height_function.png) |

### Random tiling

Using the lattice structure of set of the tilings of a given polygon and the flips between two dominoes (see [[1]](E.R)), one can generate a random tiling using the one given by the algorithm. There is three algorithms implemented to achieve this, two using random walk on the set of tilings and one using a coupling technique similar to the Propp-Wilson algorithm. With these we can observe some phenomenom like the article circle of the [aztec diamond](https://en.wikipedia.org/wiki/Aztec_diamond). Here are some example generated with the logiciel :

| Aztec Diamond | Hearth shape | Cross shape |
| --- | --- | --- |
| ![](https://github.com/YellowWaitt/domino-tiling/blob/main/images/aztec_diamond.png) | ![](https://github.com/YellowWaitt/domino-tiling/blob/main/images/hearth.png) | ![](https://github.com/YellowWaitt/domino-tiling/blob/main/images/cross.png) |

### The program

You can use the program with the jar provided or by compiling the sources yourself.

It allows you to enter any polygon by describing his boundary. You can use the letters *h*, *d*, *b* and *g* (respictively for *up*, *right*, *bottom* and *left*) and exponentitation to describe the boundary. For example the 4x4 square could be described as `d^4 h^4 g^4 bbbb`, white spaces being ignored. The polygon's boundary of the above images can be found [here](https://github.com/YellowWaitt/domino-tiling/blob/main/resources/example.xml).

Once you have a polygon you can:
- edit his boundary by double clicking on it in the left panel
- show his tiling and/or his associated height function using the *"Affichage"* menu at the top
- do flip on his tiling by clicking between two dominoes in the right panel
- generate a random tiling using the *"Mélange"* menu.

Note that if a polygon is non tilleable and the options to show his tilling is enabled, nothing will be shown in the right panel.

## References

<a id="E.R">[1]</a>
Eric Rémila, The lattice structure of the set of domino tilings of a polygon, Theoretical Computer Science 322, (2004)

<a id="J.C">[2]</a>
J. H. Conway and J. C. Lagarias, Tiling with polyominoes and combinatorial group theory, The American Mathematical Monthly Vol. 97, (1990)
