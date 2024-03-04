package main

import (
	"fmt"
	"go/ast"
	"go/format"
	"go/parser"
	"go/token"
	"os"
	"sort"
	"strings"
)

func main() {
	if len(os.Args) != 2 {
		fmt.Printf("usage: astprint <filename.go>\n")
		return
	}

	// Создаём хранилище данных об исходных файлах
	fset := token.NewFileSet()

	// Вызываем парсер
	if file, err := parser.ParseFile(
		fset,                 // данные об исходниках
		os.Args[1],           // имя файла с исходником программы
		nil,                  // пусть парсер сам загрузит исходник
		parser.ParseComments, // приказываем сохранять комментарии
	); err == nil {
		// Если парсер отработал без ошибок, печатаем дерево
		sortVars(file)
		if format.Node(os.Stdout, fset, file) != nil {
			fmt.Printf("Error: %v", err)
		}
		//ast.Fprint(os.Stdout, fset, file, nil)
	} else {
		// в противном случае, выводим сообщение об ошибке
		fmt.Printf("Error: %v", err)
	}

}

func sortVars(file *ast.File) {
	// Вызываем обход дерева, начиная от корня
	ast.Inspect(file, func(node ast.Node) bool {
		if varStatement, ok := node.(*ast.GenDecl); ok && varStatement.Tok == token.VAR {

			specs := make([]ast.Spec, 0)
			for _, spec := range varStatement.Specs {
				if valueSpec, ok := spec.(*ast.ValueSpec); ok {
					for i, name := range valueSpec.Names {
						specs = append(specs, &ast.ValueSpec{
							Names: []*ast.Ident{
								name,
							},
							Type:   valueSpec.Type,
							Values: getValue(valueSpec.Values, i),
						})
					}
				}
			}
			sort.Slice(specs, func(i, j int) bool {
				return strings.Compare(specs[i].(*ast.ValueSpec).Names[0].Name, specs[j].(*ast.ValueSpec).Names[0].Name) < 0
			})
			varStatement.Specs = specs
			return false
		}
		return true
	})
}

func getValue(values []ast.Expr, i int) []ast.Expr {
	if values == nil {
		return nil
	}
	return []ast.Expr{
		values[i],
	}

}
