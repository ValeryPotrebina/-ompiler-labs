package main

import "fmt"

func main() {
	var (
		b    string = "ded"
		e    bool   = true
		a, c int    = 2, 4
		d    int
		f, g int
	)

	var (
		ge string = "dedf"
		ga int    = 3
	)

	var n int

	n, d, f, g = 3, 4, 5, 7
	fmt.Println(b, a, c, e, d, f, g, ge, ga, n)

}
