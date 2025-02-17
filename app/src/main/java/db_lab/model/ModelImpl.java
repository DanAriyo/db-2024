package db_lab.model;

import db_lab.data.dataentity.Product;
import db_lab.data.dataentity.ProductPreview;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

// This is the real model implementation that uses the DAOs we've defined to
// actually load data from the underlying database.
//
// As you can see this model doesn't do too much except loading data from the
// database and keeping a cache of the loaded previews.
// A real model might be doing much more, but for the sake of the example we're
// keeping it simple.
//
public final class ModelImpl implements Model {

}